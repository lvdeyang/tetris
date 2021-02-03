package com.sumavision.tetris.device;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.application.alarm.service.AlarmService;
import com.sumavision.tetris.business.common.Util.CommonUtil;
import com.sumavision.tetris.business.common.Util.IpV4Util;
import com.sumavision.tetris.business.common.dao.TaskOutputDAO;
import com.sumavision.tetris.business.common.enumeration.BackType;
import com.sumavision.tetris.business.common.enumeration.BackupStrategy;
import com.sumavision.tetris.business.common.enumeration.FunUnitStatus;
import com.sumavision.tetris.business.common.enumeration.SwitchMode;
import com.sumavision.tetris.business.common.service.SyncService;
import com.sumavision.tetris.business.common.service.TaskService;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.device.backup.BackupService;
import com.sumavision.tetris.device.group.DeviceGroupDao;
import com.sumavision.tetris.device.group.DeviceGroupPO;
import com.sumavision.tetris.device.group.DeviceGroupService;
import com.sumavision.tetris.device.netcard.NetCardInfoDao;
import com.sumavision.tetris.device.netcard.NetCardInfoPO;
import com.sumavision.tetris.device.netcard.NetCardService;
import com.sumavision.tetris.device.netgroup.NetGroupDao;
import com.sumavision.tetris.device.netgroup.NetGroupPO;
import com.sumavision.tetris.device.xtool.httptool.NetCardHttpUnit;
import com.sumavision.tetris.resouce.feign.bundle.BundleFeignService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * Created by Lost on 2017/2/6.
 */
@Service
public class DeviceService {


    private static final Logger LOGGER = LoggerFactory.getLogger(DeviceService.class);

    @Autowired
    NetCardInfoDao netCardInfoDao;

    @Autowired
    DeviceDao deviceDao;

    @Autowired
    NetGroupDao netGroupDao;

    @Autowired
    NetCardHttpUnit netCardHttpUnit;
    
    @Autowired
    DeviceGroupDao deviceGroupDao;

    @Autowired
    TaskService taskService;

    @Autowired
    NetCardService netCardService;

    @Autowired
    DeviceGroupService deviceGroupService;

    @Autowired
    TaskOutputDAO taskOutputDAO;

    @Autowired
    BackupService backupService;

    @Autowired
    SyncService syncService;

    @Autowired
    BundleFeignService bundleFeignService;

    @Autowired
    AlarmService alarmService;

//    @Autowired
//    private ConstantUtil constantUtil;

//todo 暂时把操作日志的都注释
//    @Autowired
//    private AlarmReportToCmpUtil alarmReportToCmpUtil;

    public void delete(DeviceGroupPO deviceGroupPO)  {
        /**删除分组下的所有设备*/
        List<DevicePO> devicePOs = deviceDao.findByDeviceGroupId(deviceGroupPO.getId());
        for (DevicePO devicePO : devicePOs) {
            deleteDevice(devicePO);
        }
    }

    public void deleteDevice(Long deviceId){
        DevicePO devicePO = deviceDao.findById(deviceId);
        if(devicePO == null){
           return;
        }
        deleteDevice(devicePO);
    }

    public void deleteDevice(DevicePO devicePO)  {
//        alarmReportToCmpUtil.reportOptLogToCmp(AlarmConstants.OPT_DEVICE_DELETE, "delete device,id:" + deviceId);
        //todo 删设备暂不删任务
//        try {
//            taskService.removeAll(devicePO.getDeviceIp());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        /**删除设备数据*/
        deviceDao.delete(devicePO);
        List<NetCardInfoPO> netcards = netCardInfoDao.findByDeviceId(devicePO.getId());
        netCardInfoDao.deleteInBatch(netcards);
    }


    public void deviceCheck(String deviceName,String deviceIp) throws BaseException {
        if(null != deviceDao.findTopByName(deviceName)){
            throw new  BaseException( StatusCode.FORBIDDEN,"设备名不能重复");
        }
        if (null != deviceDao.findByDeviceIp(deviceIp)) {
            throw new BaseException(StatusCode.FORBIDDEN,"设备IP不能重复");
        }
    }

    /**
     * @MethodName: saveDevice
     * @Description: TODO 添加设备，只增不删
     * @param groupId 1 分组ID
     * @param backType 2 备份类型
     * @param name 3 设备名
     * @param deviceIp 4 设备IP
     * @param port 5 设备端口
     * @Return: void
     * @Author: Poemafar
     * @Date: 2020/12/29 9:23
     **/
    public void saveDevice(Long groupId, BackType backType, String name, String deviceIp, Integer port) throws BaseException {
        Map<String, String> bundleMap = new HashMap<>();
        try {
            bundleMap = bundleFeignService.addTransCodeDevice(name, deviceIp, port);
        } catch (Exception e) {
            throw new BaseException(StatusCode.ERROR,"资源服务：添加设备异常");
        }
        String bundleId = (String)bundleMap.get("bundle_id");
        if (bundleId.isEmpty()){
            throw new BaseException(StatusCode.ERROR,"资源服务：获取设备标识失败");
        }
        String bundle_set = bundleMap.get("bundle_set");

        /**保存设备数据*/
        DevicePO devicePO = new DevicePO();
        devicePO.setName(name);
        devicePO.setDeviceGroupId(groupId);
        devicePO.setBackType(backType);
        devicePO.setDeviceIp(deviceIp);
        devicePO.setDevicePort(port);
        devicePO.setBundleId(bundleId);
        if ("success".equals(bundle_set)){
            devicePO.setFunUnitStatus(FunUnitStatus.NORMAL);
        }else{
            devicePO.setFunUnitStatus(FunUnitStatus.OFF_LINE);
        }
        deviceDao.save(devicePO);
        try {
            alarmService.setAlarmUrl(deviceIp);//离线的时候就会设置失败
        } catch (Exception e) {
            LOGGER.error("设置告警地址失败",e);
        }
        try {
            setNetCardsForDevice(devicePO);
        } catch (Exception e) {
            LOGGER.error("获取设备网卡信息失败",e);
            deviceDao.delete(devicePO);
            throw new BaseException(StatusCode.ERROR,"获取设备网卡信息失败");
        }
    }

    public void saveDevice(String deviceIp){
        DeviceGroupPO deviceGroupPO = deviceGroupDao.findByBeDefault(true);
        if (deviceGroupPO == null) {
            LOGGER.warn("未找到默认分组");
            return;
        }
        try {
            saveDevice(deviceGroupPO.getId(),BackType.MAIN,deviceIp,deviceIp,5656);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setNetCardsForDevice(DevicePO devicePO) throws BaseException {
        for (NetCardInfoPO netCardInfoPO : netCardService.getNetCardInfo(devicePO)) {
            //            netCardInfoPO.setDeviceId(deviceNodePO.getId());
            if (IpV4Util.ipEquals(netCardInfoPO.getIpv4() , devicePO.getDeviceIp())
                    || IpV4Util.ipEquals(netCardInfoPO.getVirtualIpv4() , devicePO.getDeviceIp()) ) {
                netCardInfoPO.setBeCtrl(true);
            }
            netCardInfoDao.save(netCardInfoPO);
        }

    }

    /*
     * 页面初始化 单节点，controller入口
     * */
    public ResOptVO configDevice(Long deviceId , JSONArray nets) throws BaseException {

        DevicePO devicePO = deviceDao.findById(deviceId);
        //检查网卡分组配置是否正确
        ResOptVO resOptVO = checkNetGroupConfig(devicePO.getId(),nets);
        if (!resOptVO.getBeSuccess()){
            return resOptVO;
        }
        //设置告警地址
        try {
            alarmService.setAlarmUrl(devicePO.getDeviceIp());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(StatusCode.ERROR,"告警地址设置失败");
        }
        //设置小工具回调地址
        try {
            netCardService.setCallBack(devicePO);
        } catch (BaseException e) {
            e.printStackTrace();
            throw new BaseException(StatusCode.ERROR,"小工具回调地址设置失败");
        }
        //判断是否配置控制口输出
        if (checkCtrlPortAndOutput(nets)){
            resOptVO.setTooltip("控制口配置输出分组会导致，本地发布任务主备切换失败；");
        }
        netCardService.setNetGroup(nets);

        //网卡分组 配置成功
        deviceDao.updateNetConfigById(devicePO.getId(),true);
        updateDataNetIds(devicePO.getDeviceGroupId());
        return resOptVO;
    }

    /**
     * 更新设备分组上的网卡分组
     * @param deviceGroupId
     */
    public void updateDataNetIds(Long deviceGroupId){
        List<DevicePO>  devicePOS = deviceDao.findByDeviceGroupIdAndNetConfig(deviceGroupId,true);
        if (devicePOS.isEmpty()){
            deviceGroupDao.updateDataNetIdsById(deviceGroupId,"");
        }else{
            DevicePO  devicePO = devicePOS.get(0);
            List<NetGroupPO> netGroupPOS = netGroupDao.findAll();
            List<Long> dataNetIds = new ArrayList<>();
            for (int i=0;i<netGroupPOS.size();i++){
                NetGroupPO netGroupPO = netGroupPOS.get(i);
                if(getNetsFromDevice(devicePO).contains(netGroupPO.getNetName())){
                    dataNetIds.add(netGroupPO.getId());
                }else{
                    dataNetIds.add(0L);
                }
            }
            deviceGroupService.getDeviceGroupDao().updateDataNetIdsById(deviceGroupId, StringUtils.join(dataNetIds,","));
        }

    }


    public Boolean checkCtrlPortAndOutput(JSONArray nets){
        for (int i=0;i<nets.size();i++){
            JSONObject net = nets.getJSONObject(i);
            Long netCardInfoId = net.getLong("id");
            NetCardInfoPO netCardInfoPO = netCardInfoDao.findById(netCardInfoId);
            if (net.containsKey("outputNetGroupId") && net.getLong("outputNetGroupId")!=null && netCardInfoPO!=null &&  netCardInfoPO.getBeCtrl()){
                return true;
            }
        }
        return false;
    }

    public ResOptVO checkNetGroupConfig(Long deviceId , JSONArray nets) throws BaseException {
        Set<String> netGroupSet = new HashSet<>();
        DevicePO devicePO = deviceDao.findById(deviceId);
        for (int i = 0 ; i < nets.size() ; i++) {
            JSONObject net = nets.getJSONObject(i);
            Long inNet = net.getLong("inputNetGroupId");
            Long outNet = net.getLong("outputNetGroupId");

            if (inNet != null ){
                NetGroupPO inNetGroupPO = netGroupDao.findById(inNet);
                if (inNetGroupPO!=null){
                    if (netGroupSet.contains(inNetGroupPO.getNetName()) ){
                        throw new BaseException(StatusCode.FORBIDDEN,"网卡分组配置重复");
                    }else {
                        netGroupSet.add(inNetGroupPO.getNetName());
                    }
                }
            }

            if (outNet != null) {
                NetGroupPO outNetGroupPO = netGroupDao.findById(outNet);
                if (outNetGroupPO != null) {
                    if (netGroupSet.contains(outNetGroupPO.getNetName()) ){
                        throw new BaseException(StatusCode.FORBIDDEN,"网卡分组配置重复");
                    }else {
                        netGroupSet.add(outNetGroupPO.getNetName());
                    }
                }
            }
        }
        if(netGroupSet.isEmpty()){
            throw new BaseException(StatusCode.FORBIDDEN,"网卡分组不能为空");
        }
        //获取同分组下的已经配置了网卡分组的设备
        List<DevicePO> devicePOS = deviceDao.findByDeviceGroupIdAndNetConfig(devicePO.getDeviceGroupId(),true);
        if (devicePOS == null || devicePOS.isEmpty()){
            return  new ResOptVO(true);
        }else if (devicePOS.size()==1){
            DevicePO refDevice = devicePOS.get(0);
            if (refDevice.getId() == devicePO.getId()){
                return  new ResOptVO(true);
            }
            Set configedNets = getNetsFromDevice(refDevice);

            if(CommonUtil.isEqualSet(netGroupSet,configedNets)){
                return  new ResOptVO(true);
            }else{
                ResOptVO resOptVO = new ResOptVO();
                resOptVO.setBeSuccess(false);
                resOptVO.setReason("同一设备分组下的所有设备，配置的网卡分组应保持一致");
                resOptVO.setDetail("设备："+refDevice.getName()+"，已配置的网卡分组："+configedNets
                        +"\r\n当前设备配置的网卡分组："+netGroupSet
                        +"\r\n网卡分组需和已配设备的网卡分组保持一致，配置分组："+configedNets);
                return resOptVO;
            }
        }else{
            DevicePO refDevice = devicePOS.get(0);
            Set configedNets = getNetsFromDevice(refDevice);
            if (CommonUtil.isEqualSet(netGroupSet,configedNets)){
                return  new ResOptVO(true);
            }else{
                ResOptVO resOptVO = new ResOptVO();
                resOptVO.setBeSuccess(false);
                resOptVO.setReason("同一设备分组下的所有设备，配置的网卡分组应保持一致");
                resOptVO.setDetail("设备："+refDevice.getName()+"，已配置的网卡分组："+configedNets
                        +"\r\n当前设备配置的网卡分组："+netGroupSet
                        +"\r\n网卡分组需和已配置设备的分组保持一致，配置分组："+configedNets);
                return resOptVO;
            }
        }
    }

    /**
     * 查某一设备已经配置的网卡分组
     * @param devicePO
     * @return
     */
    public Set getNetsFromDevice(DevicePO devicePO){
        Set<String> netGroups = new HashSet<>();
        netCardInfoDao.findByDeviceId(devicePO.getId()).stream().forEach(net->{
            if (net.getInputNetGroupId()!=null){
                NetGroupPO netGroupPO = netGroupDao.findById(net.getInputNetGroupId());
                if (netGroupPO!=null){
                    netGroups.add(netGroupPO.getNetName());
                }
            }
            if (net.getOutputNetGroupId()!=null){
                NetGroupPO netGroupPO = netGroupDao.findById(net.getOutputNetGroupId());
                if (netGroupPO!=null){
                    netGroups.add(netGroupPO.getNetName());
                }
            }
        });
        return netGroups;
    }

    public void editDevice(Long id,Long groupId,String name,String backTypeStr) throws Exception {
        DevicePO one = deviceDao.findById(id);
        if (one==null){
            throw new BaseException(StatusCode.FORBIDDEN,"未找到设备");
        }
        BackType backType = BackType.valueOf(backTypeStr.toUpperCase(Locale.ENGLISH));
        if (!one.getBackType().equals(backType)&&backType.equals(BackType.BACK)){
            //删该设备上的所有任务
            taskService.removeAll(one.getDeviceIp());
        }
        one.setDeviceGroupId(groupId);
        one.setName(name);
        one.setBackType(backType);
        deviceDao.save(one);
    }

    /*
     * 页面重置设备，controller入口
     * */
    public boolean resetDevice(Long deviceId){
        LOGGER.info("resetDevice id:{}",deviceId);
        DevicePO devicePO = deviceDao.findById(deviceId);
        netCardService.resetNetCardNetGroup(devicePO.getId());
        deviceDao.updateNetConfigById(deviceId,false);
        updateDataNetIds(devicePO.getDeviceGroupId());

        return true;
    }

    public void refreshNetcard(Long id) throws BaseException {
        DevicePO devicePO = deviceDao.findById(id);
        if (devicePO != null) {
            netCardService.getNetCardInfo(devicePO);//不更新控制口
            NetCardInfoPO netCardInfoPO = netCardInfoDao.findByIpv4OrVirtualIpv4(devicePO.getDeviceIp() , devicePO.getDeviceIp());
            netCardInfoPO.setBeCtrl(true);
            netCardInfoDao.save(netCardInfoPO);
        }
    }

    public DevicePO getDeviceByIdWithNullCheck(Long id) throws BaseException {
        DevicePO one = deviceDao.findById(id);
        if (one==null){
            throw new BaseException(StatusCode.FORBIDDEN,"设备不存在");
        }
        return one;
    }

    /**
     * 手动设备切换
     * @param srcDevId
     * @param tgtDevId
     */
    public synchronized void switchDeviceByManual(Long srcDevId,Long tgtDevId) throws Exception {

        DevicePO srcDev = getDeviceByIdWithNullCheck(srcDevId);
        DevicePO tgtDev = getDeviceByIdWithNullCheck(tgtDevId);


        DeviceGroupPO deviceGroup = deviceGroupDao.findById(srcDev.getDeviceGroupId());
        if (BackupStrategy.NPLUSM.equals(deviceGroup.getBackupStrategy())) {
            //N+M 手动切换
            SwitchMode mode = SwitchMode.MASTER2SLAVE;
            if (BackType.MAIN.equals(srcDev.getBackType())&&BackType.BACK.equals(tgtDev.getBackType())){
                mode=SwitchMode.MASTER2SLAVE;
            }
            if (BackType.BACK.equals(srcDev.getBackType())&&BackType.MAIN.equals(tgtDev.getBackType())){
                mode=SwitchMode.SLAVE2MASTER;
            }
            backupService.triggerManualSwitchDevice(srcDev,tgtDev, mode);

        }else if (BackupStrategy.ONE2ONE.equals(deviceGroup.getBackupStrategy())){
            //1+1手动切换


        }

    }



    public void syncDevice(Long id) throws Exception {
        DevicePO device = getDeviceByIdWithNullCheck(id);
        syncService.syncTransform(device.getDeviceIp());
        deviceDao.updateFunUnitStatusById(FunUnitStatus.NORMAL,id);
    }

}
