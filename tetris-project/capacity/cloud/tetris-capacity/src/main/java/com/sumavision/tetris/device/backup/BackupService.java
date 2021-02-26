package com.sumavision.tetris.device.backup;/**
 * Created by Poemafar on 2020/12/24 18:01
 */

import com.sumavision.tetris.alarm.clientservice.http.AlarmFeignClientService;
import com.sumavision.tetris.application.alarm.AlarmCode;
import com.sumavision.tetris.business.common.dao.TaskInputDAO;
import com.sumavision.tetris.business.common.dao.TaskOutputDAO;
import com.sumavision.tetris.business.common.enumeration.BackType;
import com.sumavision.tetris.business.common.enumeration.FunUnitStatus;
import com.sumavision.tetris.business.common.enumeration.SwitchMode;
import com.sumavision.tetris.business.common.po.TaskOutputPO;
import com.sumavision.tetris.business.common.service.SyncService;
import com.sumavision.tetris.business.common.service.TaskService;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.device.DeviceDao;
import com.sumavision.tetris.device.DevicePO;
import com.sumavision.tetris.device.backup.condition.BackupConditionPO;
import com.sumavision.tetris.device.group.DeviceGroupDao;
import com.sumavision.tetris.device.group.DeviceGroupPO;
import com.sumavision.tetris.device.netcard.NetCardInfoPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @ClassName: BackupOperate
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/12/24 18:01
 */
@Component
public class BackupService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BackupService.class);


    @Autowired
    TaskService taskService;

    @Autowired
    DeviceDao deviceDao;

    @Autowired
    SyncService syncService;

    @Autowired
    TaskOutputDAO taskOutputDAO;

    @Autowired
    TaskInputDAO taskInputDAO;

    @Autowired
    SwitchCtrlUnit switchCtrlUnit;

    @Autowired
    DeviceGroupDao deviceGroupDao;

    @Autowired
    AlarmFeignClientService alarmFeignClientService;

    /**
     * 设备切换，核心逻辑
     * @param srcDev
     * @param tgtDev
     * @param mode
     * @throws Exception
     */
    public void switchDevice(DevicePO srcDev, DevicePO tgtDev, SwitchMode mode) throws Exception {
        if (SwitchMode.MASTER2SLAVE.equals(mode)) {
            switchCtrlUnit.switchDeviceInDB(srcDev, tgtDev);
        }else if (SwitchMode.SLAVE2MASTER.equals(mode)){
            switchCtrlUnit.switchDeviceInDB(tgtDev,srcDev);
        }
        //发同步
        syncTransformByAsync(srcDev);
        syncTransformByAsync(tgtDev);
    }

    public void syncTransformByAsync(DevicePO device){
        new Thread(()->{
            try {
                syncService.syncTransform(device.getDeviceIp(),device.getDevicePort());
            } catch (Exception e) {
                //同步失败但主上的任务数据已经没了，此时会主备同输出，上报不同步？
                if (FunUnitStatus.NORMAL.equals(device.getFunUnitStatus())) {
                    deviceDao.updateFunUnitStatusByIp(FunUnitStatus.NEED_SYNC, device.getDeviceIp());
                    LOGGER.warn("不同步，"+device.getDeviceIp(),e);
                }
            }
        }).start();
    }



    /**
     * 触发手动主备切换
     * 单播任务也切，就是会断流告警
     * @param srcDev
     * @param tgtDev
     */
    public synchronized void triggerManualSwitchDevice(DevicePO srcDev, DevicePO tgtDev, SwitchMode mode) throws Exception {
        String srcIp = srcDev.getDeviceIp();
        String tgtIp = tgtDev.getDeviceIp();

        checkSwitchDeviceByManual(srcDev,tgtDev);
        switchDevice(srcDev,tgtDev,mode);

        //切换完成后需要报告警
        String alarmObj = "";
        Map<String,String> params = new HashMap<>();
        params.put("srcDevice",srcIp);
        params.put("dstDevice",tgtIp);
        params.put("method","MANUAL");
        params.put("strategy","N+M");
        params.put("mode",mode.name());
        alarmFeignClientService.triggerAlarm(AlarmCode.DEVICE_SWITCH,srcIp,alarmObj,params,Boolean.TRUE,new Date());

    }

    /**
     * 触发自动切换
     * @param deviceIp
     */
    public synchronized void triggerAutoBackup(String deviceIp){
        DevicePO srcDev = deviceDao.findByDeviceIp(deviceIp);
        if (srcDev==null){
            LOGGER.info("找不到设备,{}",deviceIp);
            return;
        }
        if (!checkSwitchDeviceByAuto(srcDev)){
            return;
        }

        List<DevicePO> tgtDevices = findSlaveDeviceForSwitch(srcDev);
        if (tgtDevices==null || tgtDevices.isEmpty()){
            LOGGER.info("无可用备机");
            return;
        }
        DevicePO tgtDev = null;
        for (int i = 0; i < tgtDevices.size(); i++) {
            DevicePO curSlaveDev = tgtDevices.get(i);
            try {
                switchDevice(srcDev,curSlaveDev,SwitchMode.MASTER2SLAVE);
                tgtDev=curSlaveDev;
                break;
            } catch (Exception e) {
                LOGGER.info("切换失败，主：{}，备：{}",srcDev.getDeviceIp(),curSlaveDev.getDeviceIp());
            }
        }
        if (tgtDev!=null){
            LOGGER.info("自动切换成功，主：{}，备：{}",srcDev.getDeviceIp(),tgtDev.getDeviceIp());
            String alarmObj = "";
            Map<String,String> params = new HashMap<>();
            params.put("srcDevice",srcDev.getDeviceIp());
            params.put("dstDevice",tgtDev.getDeviceIp());
            params.put("method","AUTO");
            params.put("strategy","N+M");
            params.put("mode",SwitchMode.MASTER2SLAVE.name());
            try {
                alarmFeignClientService.triggerAlarm(AlarmCode.DEVICE_SWITCH,srcDev.getDeviceIp(),alarmObj,params,Boolean.TRUE,new Date());
            } catch (Exception e) {
                LOGGER.error("告警上报失败",e);
            }
        }else{
            LOGGER.error("自动切换失败，主：{}",srcDev.getDeviceIp());
        }

    }


    public Boolean checkSwitchDeviceByAuto(DevicePO devicePO){

        if (BackType.BACK.equals(devicePO.getBackType())){
            LOGGER.info("备机不自动切换，{}",devicePO.getBackType());
            return false;
        }
        DeviceGroupPO deviceGroupPO = deviceGroupDao.findById(devicePO.getDeviceGroupId());
        if (deviceGroupPO!=null && !deviceGroupPO.getAutoBackupFlag()){
            LOGGER.info("设备分组{}自动备份未开",deviceGroupPO.getName());
            return false;
        }
        return true;
    }


    //todo 找可用备机,,,切失败了就往下一个切
    public List<DevicePO> findSlaveDeviceForSwitch(DevicePO masterDevice){
        List<DevicePO> slaveDevices = deviceDao.findByDeviceGroupIdAndBackTypeAndFunUnitStatusAndNetConfig(masterDevice.getDeviceGroupId(), BackType.BACK,FunUnitStatus.NORMAL, Boolean.TRUE);
        if (slaveDevices == null || slaveDevices.isEmpty()){
            LOGGER.error("no configed normal slave device at the same device group");
            return null;
        }
        return slaveDevices;
    }




    /**
     * 检查下是否能切
     * @param srcDev
     * @param tgtDev
     */
    public void checkSwitchDeviceByManual(DevicePO srcDev,DevicePO tgtDev) throws BaseException {
        if (BackType.DEFAULT.equals(srcDev)||BackType.DEFAULT.equals(tgtDev)){
            throw new BaseException(StatusCode.FORBIDDEN,"无法切换：主备状态异常");
        }
        if (srcDev.getBackType() == tgtDev.getBackType()){
            throw new BaseException(StatusCode.FORBIDDEN,"无法切换：主备类型相同");
        }
        if (!srcDev.getNetConfig()||!tgtDev.getNetConfig()){
            throw new BaseException(StatusCode.FORBIDDEN,"无法切换：设备未配置");
        }
        if (!FunUnitStatus.NORMAL.equals(tgtDev.getFunUnitStatus())){
            throw new BaseException(StatusCode.FORBIDDEN,"无法切换：目标设备异常");
        }
        if (BackType.BACK.equals(srcDev.getBackType())) {
            Integer taskNum = taskOutputDAO.countDistinctByCapacityIpAndOutputNotNullAndTaskNotNull(srcDev.getDeviceIp());
            if (taskNum!=null&&taskNum>0){
                throw new BaseException(StatusCode.FORBIDDEN,"无法切换：源备机存在任务");
            }
        }
        if (BackType.BACK.equals(tgtDev.getBackType())) {
            Integer taskNum = taskOutputDAO.countDistinctByCapacityIpAndOutputNotNullAndTaskNotNull(tgtDev.getDeviceIp());
            if (taskNum!=null&&taskNum>0){
                throw new BaseException(StatusCode.FORBIDDEN,"无法切换：目标备机存在任务");
            }
        }
    }




}
