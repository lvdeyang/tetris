package com.sumavision.tetris.sts.device;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.resouce.feign.bundle.BundleFeign;
import com.sumavision.tetris.sts.common.CommonConstants.*;
import com.sumavision.tetris.sts.common.CommonUtil;
import com.sumavision.tetris.sts.common.ErrorCodes;
import com.sumavision.tetris.sts.communication.httptool.NetCardHttpUnit;
import com.sumavision.tetris.sts.device.auth.DeviceChannelAuthDao;
import com.sumavision.tetris.sts.device.auth.EncapsulateAuthPO;
import com.sumavision.tetris.sts.device.group.DeviceGroupDao;
import com.sumavision.tetris.sts.device.group.DeviceGroupPO;
import com.sumavision.tetris.sts.device.monitor.DeviceMonitorService;
import com.sumavision.tetris.sts.device.netcard.NetCardInfoDao;
import com.sumavision.tetris.sts.device.netcard.NetCardInfoPO;
import com.sumavision.tetris.sts.device.netcard.NetCardService;
import com.sumavision.tetris.sts.device.node.DeviceNodeDao;
import com.sumavision.tetris.sts.device.node.DeviceNodePO;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * Created by Lost on 2017/2/6.
 */
@Service
public class DeviceService {

    static Logger logger = LogManager.getLogger(DeviceService.class);

    @Autowired
    NetCardInfoDao netCardInfoDao;

    @Autowired
    DeviceNodeDao deviceNodeDao;

    @Autowired
    NetCardService netCardService;
    
    @Autowired
    DeviceMonitorService deviceMonitorService;

    @Autowired
    DeviceCommunication deviceCommunication;

//    @Autowired
//    SourceService sourceService;
    
    @Autowired
    NetCardHttpUnit netCardHttpUnit;
    
    @Autowired
    DeviceGroupDao deviceGroupDao;
    
//    @Autowired
//    ComplexService complexService;
    
    @Autowired
    DeviceChannelAuthDao deviceChannelAuthDao;

//todo 暂时把告警的都注释
//    @Autowired
//    AlarmService alarmService;
    
    @Autowired
    DeviceDao deviceDao;

    @Autowired
    private BundleFeign bundleFeign;

//    @Autowired
//    private ConstantUtil constantUtil;

//todo 暂时把操作日志的都注释
//    @Autowired
//    private AlarmReportToCmpUtil alarmReportToCmpUtil;


    public void saveDevice(Long groupId, String name, DeviceType deviceType, String deviceIp, BackType backType) throws BaseException {

//    	alarmReportToCmpUtil.reportOptLogToCmp(AlarmConstants.OPT_DEVICE_ADD,"add device, name:"+name+" ip:"+deviceIp+" type:"+deviceType);

        /**name check*/
    	deviceNameCheck(name);
    	
    	/**保存设备数据*/
    	DevicePO devicePO = new DevicePO();
    	devicePO.setName(name);
    	devicePO.setGroupId(groupId);
    	devicePO.setDeviceType(deviceType);
    	devicePO.setBackType(backType);
    	deviceDao.save(devicePO);
    	
    	/**如果是服务器设备，保存其上的能力数据*/
    	if(DeviceType.SERVER == deviceType){
    		DeviceNodePO deviceNodePO = new DeviceNodePO();
    		deviceNodePO.setDeviceGroupId(groupId);
    		deviceNodePO.setDeviceId(devicePO.getId());
    		deviceNodePO.setDeviceIp(deviceIp);
    		deviceNodePO.setName(deviceIp);
//    		deviceNodePO.setTransBackType(transBackType);
    		saveNode(deviceNodePO);
    	}
    }
    
    public void deviceNameCheck(String deviceName) throws BaseException {
    	if(null != deviceDao.findTopByName(deviceName)){
    		throw new BaseException(StatusCode.FORBIDDEN, ErrorCodes.NAME_CONFLICT);
    	}
    }
    
    public void deviceNameUpdateCheck(String deviceName,Long deviceId) throws BaseException {
    	DevicePO device = deviceDao.findTopByName(deviceName);
    	if(null != device && !device.getId().equals(deviceId)){
    		throw new BaseException(StatusCode.FORBIDDEN,ErrorCodes.NAME_CONFLICT);
    	}
    }

    public void saveNode(DeviceNodePO deviceNodePO) throws BaseException {
        saveCheck(deviceNodePO);
        deviceNodeDao.save(deviceNodePO);
//        matchSubNet(deviceCommunication.getNetCardInfo(deviceNodePO))
        for (NetCardInfoPO netCardInfoPO : netCardService.getNetCardInfo(deviceNodePO)) {
//            netCardInfoPO.setDeviceId(deviceNodePO.getId());
            if (CommonUtil.ipEquals(netCardInfoPO.getIpv4() , deviceNodePO.getDeviceIp())
                    || CommonUtil.ipEquals(netCardInfoPO.getVirtualIpv4() , deviceNodePO.getDeviceIp()) ) {
                netCardInfoPO.setBeCtrl(true);
            }
                
            netCardInfoDao.save(netCardInfoPO);
        }
        netCardService.setCallBack(deviceNodePO);
        deviceCommunication.sendNtp(deviceNodePO);
        deviceMonitorService.startMonitor(deviceNodePO.getDeviceIp());
    }


    /*
    *  初始化SDM设备
    * */
    public void initSdmDevice(Long sdmDeviceId , Long[] backIds) {
        List<DeviceNodePO> deviceNodePOS = deviceNodeDao.findByDeviceId(sdmDeviceId);
        if(null != backIds && backIds.length > 0){
            deviceNodePOS.forEach(deviceInfoPO -> {
                if (deviceInfoPO.getId().equals(backIds[0]) ) {
                    deviceInfoPO.setBackIndex(1);
                } else if (deviceInfoPO.getId().equals(backIds[1])) {
                    deviceInfoPO.setBackIndex(2);
                } else if (deviceInfoPO.getId().equals(backIds[2])) {
                    deviceInfoPO.setBackIndex(1);
                } else if (deviceInfoPO.getId().equals(backIds[3])) {
                    deviceInfoPO.setBackIndex(2);
                } else {
                    deviceInfoPO.setBackIndex(null);
                }
            });
        }

        try {
            for (DeviceNodePO deviceNodePO : deviceNodePOS) {
//                funUnitService.init(deviceNodePO);
            }
            deviceNodeDao.save(deviceNodePOS);
            deviceNodePOS.stream().forEach(deviceInfoPO -> deviceCommunication.initDevice(deviceInfoPO));
        } catch (Exception e) {
            reset(deviceNodePOS);
            throw e;
        }
    }

    /*
    * 重置SDM设备
    * */
    public void resetSdmDevice(Long sdmDeviceId) {
        List<DeviceNodePO> deviceNodePOS = deviceNodeDao.findByDeviceId(sdmDeviceId);
        reset(deviceNodePOS);
    }

    /*
    * 重置设备信息，删除所有功能单元
    * */
    private void reset(List<DeviceNodePO> deviceNodePOS) {
//        for (DeviceNodePO deviceNodePO : deviceNodePOS) {
//        	deviceNodePO.getDeviceChannelAuthPOs().clear();
//            funUnitService.delete(deviceNodePO);
////            deviceNodePO.setTransBackType(BackType.DEFAULT);
////            deviceNodePO.setEncapsulateBackType(BackType.DEFAULT);
////            deviceNodePO.setBackIndex(null);
//        }
//        deviceNodeDao.save(deviceNodePOS);
//yzx add and up add comment
        for (DeviceNodePO deviceNodePO : deviceNodePOS) {
            resetDevice(deviceNodePO);
        }
    }

    public void resetDevice(Long deviceId){
        DeviceNodePO deviceNodePO = deviceNodeDao.findOne(deviceId);
        resetDevice(deviceNodePO);
    }

    private void resetDevice(DeviceNodePO deviceNodePO)  {
//        funUnitService.delete(deviceNodePO);
//        sourceService.cleanSDISource(deviceNodePO.getId());
        deviceNodeDao.save(deviceNodePO);
        //重置网卡分组
        netCardService.resetNetCardNetGroup(deviceNodePO.getId());
        //转码授权清除
        deviceNodePO.getDeviceChannelAuthPOs().clear();
        //网关授权清除
        EncapsulateAuthPO encapsulateAuthPO = deviceNodePO.getEncapsulateAuthPO();
        encapsulateAuthPO.setEncapsulateAuthJson(null);
        encapsulateAuthPO.setOutputNum(null);
        encapsulateAuthPO.setOutputRestNum(null);
        deviceNodePO.setEncapsulateAuthPO(encapsulateAuthPO);
        deviceNodeDao.save(deviceNodePO);
    }


    /*
    * 删除SDM设备分组或者SDM设备
    * */
    public void delete(DeviceGroupPO deviceGroupPO)   {
        delete(deviceGroupPO.getId() , true);
    }

    private void delete(Long deviceGroupId, boolean delete)   {
    	/**删除分组下的所有设备*/
    	List<DevicePO> devicePOs = deviceDao.findByGroupId(deviceGroupId);
    	for (DevicePO devicePO : devicePOs) {
    		deleteDevice(devicePO.getId());
		}
    }
    
    public void deleteDevice(Long deviceId)  {
//        alarmReportToCmpUtil.reportOptLogToCmp(AlarmConstants.OPT_DEVICE_DELETE, "delete device,id:" + deviceId);

        /**删除设备上的能力*/
    	List<DeviceNodePO> deviceNodePOS = deviceNodeDao.findByDeviceId(deviceId);
//        for (DeviceNodePO deviceNodePO : deviceNodePOS) {
//            TaskLinkDaoService taskLinkDaoService = SpringBeanFactory.getBean(TaskLinkDaoService.class);
//            // yzx add 恢复设备上的全部告警，
//            //alarmService.deviceAlarmRecover(deviceNodePO.getId());//若恢复操作还在队列的时候把节点删了，恢复会失败，该方法会失效
//            //之所以判断下功能单元，是因为sdm设备节点存在非主节点上没网关
//            if (deviceNodePO.getEncapsulateSocketAddress()!=null){
//                alarmService.funUnitAllRecover(deviceNodePO.getEncapsulateSocketAddress());
//            }
//            if (deviceNodePO.getTransSocketAddress()!=null){
//                alarmService.funUnitAllRecover(deviceNodePO.getTransSocketAddress());
//            }
//            taskLinkDaoService.deleteTaskByDeviceID(deviceId);//yzx add 删除设备上的任务记录，参数为设备节点id
//            delete(deviceNodePO, true);
//        }

        /**删除设备数据*/
		deviceDao.delete(deviceId);
    }

    /*
    * 删除设备
    * */
    public void delete(Long id)  {
        DeviceNodePO deviceNodePO = deviceNodeDao.findOne(id);
        delete(deviceNodePO);
    }

    public void delete(DeviceNodePO deviceNodePO)  {
        delete(deviceNodePO, true);
    }

    private void delete(DeviceNodePO deviceNodePO, boolean delete)  {
//        funUnitService.delete(deviceNodePO, delete);
        netCardInfoDao.delete(netCardInfoDao.findByDeviceId(deviceNodePO.getId()));
        netCardService.cleanCallBack(deviceNodePO);
//        sourceService.cleanSDISource(deviceNodePO.getId());
        deviceNodeDao.delete(deviceNodePO);
        deviceMonitorService.stopMonitor(deviceNodePO.getDeviceIp());
    }

    /*
    *  单节点初始化，目前没用
    * */
    public void init(Long deviceId)  {
        DeviceNodePO deviceNodePO = deviceNodeDao.findOne(deviceId);
        init(deviceNodePO);
    }

    public void init(DeviceNodePO deviceNodePO) {
//        funUnitService.init(deviceNodePO);
        deviceNodeDao.save(deviceNodePO);
        deviceCommunication.initDevice(deviceNodePO);
    }

    private List<NetCardInfoPO> matchSubNet(List<NetCardInfoPO> netCardInfoPOs){
    /*    List<NetGroupPO> netGroupPOs = netGroupDao.findAll();
        for (NetCardInfoPO netCardInfoPO : netCardInfoPOs) {
            for (NetGroupPO netGroupPO : netGroupPOs) {
                if (CommonUtil.matchSubNet(netCardInfoPO , netGroupPO)) {
                    netCardInfoPO.setNetGroupId(netGroupPO.getId());
                    break;
                }
            }
        }*/
        return netCardInfoPOs;
    }

    private void saveCheck(DeviceNodePO deviceNodePO) throws BaseException {
        deviceNodePO.setDeviceIp(CommonUtil.ipFormat(deviceNodePO.getDeviceIp()));
        if (null != deviceNodeDao.findByDeviceIp(deviceNodePO.getDeviceIp())) {
            throw new BaseException(StatusCode.FORBIDDEN,ErrorCodes.DEVICE_CONFLICT);
        }
        if (null != netCardInfoDao.findByIpv4OrVirtualIpv4(deviceNodePO.getDeviceIp() , deviceNodePO.getDeviceIp())){
            throw new BaseException(StatusCode.FORBIDDEN,ErrorCodes.DEVICE_CONFLICT);
        }
    }

    public DeviceDao getDeviceDao(){
        return deviceDao;
    }

	public DeviceNodeDao getDeviceNodeDao() {
		return deviceNodeDao;
	}

	public List<NetCardInfoPO> refreshNetcard(Long id) throws BaseException {
		DeviceNodePO deviceNodePO = deviceNodeDao.findOne(id);
		if (deviceNodePO != null) {
			if (deviceNodePO.getFunUnitStatus() == FunUnitStatus.NONE ) {
				//无功能单元，直接更新
				netCardService.getNetCardInfo(deviceNodePO);
                NetCardInfoPO netCardInfoPO = netCardInfoDao.findByIpv4OrVirtualIpv4(deviceNodePO.getDeviceIp() , deviceNodePO.getDeviceIp());
                netCardInfoPO.setBeCtrl(true);
                netCardInfoDao.save(netCardInfoPO);
                return netCardInfoDao.findByDeviceId(id);
			}else {
				//主动上报网口告警标志
				Boolean flag = false;
				List<NetCardInfoPO> oldCardInfoPOs = netCardInfoDao.findByDeviceId(id);
				List<NetCardInfoPO> newCardInfoPOs = netCardHttpUnit.getNetCardInfo(deviceNodePO);
				for (NetCardInfoPO old : oldCardInfoPOs) {
					NetCardInfoPO n_ = getSameNet(old , newCardInfoPOs);
			        if (n_ == null) {
			        	old.setStatus(old.getStatus() == 1 ? 0:1);
			        	if (!flag) {
			        		flag = true;
						}
			        }
			    }
				
				if (flag) {
					JSONObject statusJsonObject = JSONObject.parseObject(netCardHttpUnit.getNetcardStatus());
		        	statusJsonObject.put("type", "netchange");
//		        	alarmService.netcardNoticeHandle(statusJsonObject.toString(), deviceNodePO.getId());
				}
                return oldCardInfoPOs;
			}
			
		}
		return null;
	}
	
	private NetCardInfoPO getSameNet(NetCardInfoPO one , List<NetCardInfoPO> netCardInfoPOS) {
		for (NetCardInfoPO n : netCardInfoPOS) {
			if (n.getName().equals(one.getName()) && n.getMac().equals(one.getMac()) && n.getStatus().equals(one.getStatus()))
				return n;
        }
        return null;
    }

    public List<DevicePO> queryDeviceByName(String keyWord){
        return deviceDao.findByNameContaining(keyWord);
    }

    public List<DeviceNodePO> queryDeviceNodeByDeviceId(Long deviceId){
        return deviceNodeDao.findByDeviceId(deviceId);
    }

    public List<NetCardInfoPO> queryNetCardByDeviceNodeId(Long deviceNodeId){
        return netCardInfoDao.findByDeviceId(deviceNodeId);
    }

    public JSONArray getDeviceNodeArray(Long deivceId){
        List<DeviceNodePO> deviceNodePOS = queryDeviceNodeByDeviceId(deivceId);
        JSONArray deviceNodeArray = new JSONArray();
        deviceNodePOS.stream().forEach(node->{
            JSONObject nodeObject = new JSONObject();
            nodeObject.put("nodeId",node.getId());
            nodeObject.put("deviceNodeIP",node.getDeviceIp());
            nodeObject.put("deviceStatus","online");// 无法判断设备离线
            nodeObject.put("netCardList",getNetCardArray(node.getId()));
            deviceNodeArray.add(nodeObject);
            nodeObject = null;
        });
        return deviceNodeArray;
    }

    public JSONArray getNetCardArray(Long nodeId){
        List<NetCardInfoPO> netCardInfoPOS = queryNetCardByDeviceNodeId(nodeId);
        JSONArray netCardInfoArray = new JSONArray();
        netCardInfoPOS.stream().forEach(netcard->{
            JSONObject netObject = new JSONObject();
            netObject.put("ip",netcard.getIpv4());
            netObject.put("name",netcard.getName());
            netObject.put("mask",netcard.getIpv4Mask());
            netObject.put("gateway",netcard.getIpv4Gate());
            netCardInfoArray.add(netObject);
            netObject = null;
        });
        return netCardInfoArray;
    }

    public void queryDeviceByKeyWord(String keyword, Map<String,Object> data){
        List<DevicePO> devicePOS = queryDeviceByName(keyword);
        data.put("totalCount", devicePOS.size());
        JSONArray deviceJsonArray = new JSONArray();
        devicePOS.stream().forEach(device->{
            JSONObject deviceJsonObject = new JSONObject();
            deviceJsonObject.put("deviceName",device.getName());
            String deviceType = "";
            if (device.getDeviceType().equals(DeviceType.SDM2)){
                deviceType = "SDM2.0";
            }else if (device.getDeviceType().equals(DeviceType.SDM3)){
                deviceType = "SDM3.0";
            }else{
                deviceType = "SERVER";
            }
            deviceJsonObject.put("deviceType",deviceType);
            deviceJsonObject.put("deviceNodeList",getDeviceNodeArray(device.getId()));
            deviceJsonArray.add(deviceJsonObject);
            deviceJsonObject=null;
        });
        data.put("deviceList", deviceJsonArray);
    }

    /**
     * 更新转码授权
     * 新授权会完全覆盖旧授权，旧授权多的删除，少的增加，变化的更新[变化指的同一设备的同卡同通道的路数变化]
     * 一种情况更新失败：
     *     旧的授权通道已用，但是对应新的授权通道没有或者路数少于已用的
     * 注意：更新授权时，旧授权ID不能变，授权ID会和任务关联
     * @param deviceNodeId 表示更新转码授权的设备节点ID

     */
//    public void updateTransAuth(Long deviceNodeId) throws CommunicationException, CommonException {
//        //找节点
//        DeviceNodePO deviceNodePO = deviceNodeDao.findOne(deviceNodeId);
//        //读授权,解析授权
//        PassAuth newPassAuth = (PassAuth)funUnitCommunication.getAuth(deviceNodePO, CommonConstants.FunUnitType.TRANS);
//        if(newPassAuth.getDeviceNum() != 0){
//            //测试阶段，注释能力校验
//            //passAuth.channelCheck();
//        }
//        //找节点授权
//        List<DeviceChannelAuthPO> oldDeviceChannelAuthPOs = deviceNodePO.getDeviceChannelAuthPOs();
//        //更新授权，，，旧授权变更[list 遍历删除，需用迭代器或最常用的循环方式，不能用foreach，stream不能抛异常]
//        DeviceChannelAuthPO rtAuth = null;
//        for(Iterator<DeviceChannelAuthPO> oldAuthIter = oldDeviceChannelAuthPOs.iterator();oldAuthIter.hasNext();){
//            DeviceChannelAuthPO oldAuth = oldAuthIter.next();
//            rtAuth = null;
//            for (DeviceChannelAuthPO newAuth : newPassAuth.allDeviceChannelAuthPO()) {
//                if (oldAuth.getInputType().equals(newAuth.getInputType())
//                        && oldAuth.getOutputType().equals(newAuth.getOutputType())
//                        && oldAuth.getCardNumber().equals(newAuth.getCardNumber())){
//                    rtAuth = newAuth;
//                    break;
//                }
//            }
//            if (null == rtAuth){//没找到对应的新授权
//                if (!oldAuth.getAuthNum().equals(oldAuth.getAuthNumRest())){
//                    logger.error("used old auth [id:{}] cannot found new auth",oldAuth.getId());
//                    throw new CommonException(exceptionI18Message.getLocaleMessage(ErrorCodes.UPDATE_TRANS_AUTH_MATCH_ERROR));
//                }else {//删除授权
//                    oldAuthIter.remove();
//                }
//            }else{
//                if (rtAuth.getAuthNum()<oldAuth.getAuthNum()-oldAuth.getAuthNumRest()){
//                    logger.error("old auth [id:{}] cannot match with new auth",oldAuth.getId());
//                    throw new CommonException(exceptionI18Message.getLocaleMessage(ErrorCodes.UPDATE_TRANS_AUTH_MATCH_ERROR));
//                }else {//更新授权
//                    oldAuth.setAuthNumRest(rtAuth.getAuthNum() - oldAuth.getAuthNum() + oldAuth.getAuthNumRest());
//                    oldAuth.setAuthNum(rtAuth.getAuthNum());
//                }
//            }
//        }
//
//        //旧授权加新授权差异
//        for (DeviceChannelAuthPO newAuth : newPassAuth.allDeviceChannelAuthPO()) {
//            rtAuth = null;
//            for (DeviceChannelAuthPO oldAuth : oldDeviceChannelAuthPOs) {
//                if (oldAuth.getInputType().equals(newAuth.getInputType())
//                        && oldAuth.getOutputType().equals(newAuth.getOutputType())
//                        && oldAuth.getCardNumber().equals(newAuth.getCardNumber())){
//                    rtAuth = oldAuth;
//                    break;
//                }
//            }
//            if (null == rtAuth){//新增的授权，直接加
//                oldDeviceChannelAuthPOs.add(newAuth);
//            }
//        }
//        deviceNodePO.setnCardNum(newPassAuth.getDeviceNum());
//
//    }
}
