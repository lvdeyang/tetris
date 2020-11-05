package com.sumavision.tetris.sts.device.backup;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.sts.common.CommonConstants;
import com.sumavision.tetris.sts.common.ErrorCodes;
import com.sumavision.tetris.sts.common.SpringBeanFactory;
import com.sumavision.tetris.sts.device.DeviceDao;
import com.sumavision.tetris.sts.device.DevicePO;
import com.sumavision.tetris.sts.device.backup.condition.BackupConditionDao;
import com.sumavision.tetris.sts.device.backup.condition.BackupConditionPO;
import com.sumavision.tetris.sts.device.netcard.NetCardInfoDao;
import com.sumavision.tetris.sts.device.netcard.NetCardInfoPO;
import com.sumavision.tetris.sts.device.node.DeviceNodeDao;
import com.sumavision.tetris.sts.device.node.DeviceNodePO;
import com.sumavision.tetris.sts.netgroup.NetGroupDao;
import com.sumavision.tetris.sts.netgroup.NetGroupPO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Poemafar on 2019/5/10 18:43
 * 设备级备份服务
 * 主备属性由目前的节点级提升到设备级，服务器、SDM2.0、SDM3.0均为整台设备，设备内的任何一个节点满足触发备份的条件，整台设备触发主备切换。
 * 从业务上，添加的设备不再表示实际硬件形态，例如：SDM2.0的6个节点，如果按整台来用，就是一台设备，如果接满线按单节点用，则是6台设备。
 */
@Service
public class DeviceBackupService {

    private final Logger logger = LogManager.getLogger(DeviceBackupService.class);
    
    @Autowired
    private BackupConditionDao backupConditionDao;
    
    @Autowired
    private DeviceNodeDao deviceNodeDao;
    
//    @Autowired
//    TotalOptControl totalOptControl;
    @Autowired
    private DeviceDao deviceDao;
    @Autowired
    private NetGroupDao netGroupDao;
    @Autowired
    private NetCardInfoDao netCardInfoDao;

    /**
     * 触发自动备份的异常类型
     */
    public enum AutoBackupType {
        CPU_OVERRIDE,GPU_OVERRIDE, CTRL_PORT_DISCONNECT
    }

    /**
     * 触发手动备份操作，验证主备机是否正常，主备切换方法在backupOpreate类的downMasterToStandby
     * @param masterDeviceId 主机ID
     * @param standbyDeviceId 备机ID
     */
    public void triggerManualBackup(Long masterDeviceId, Long standbyDeviceId) throws BaseException {

        logger.info("manual switch master-standby begin, masterDeviceId : {}, standbyDeviceId: {}", masterDeviceId,standbyDeviceId);
        DeviceDao deviceDao = SpringBeanFactory.getBean(DeviceDao.class);
        DevicePO masterDevice = deviceDao.findOne(masterDeviceId);
        DevicePO standbyDevice = deviceDao.findOne(standbyDeviceId);
        if (masterDevice == null) {
            logger.error("switch master-standby fail --- masterDevice is null");
            throw new BaseException(StatusCode.ERROR,ErrorCodes.CANNOT_FIND_MASTER_DEVICE);
        }
        if (standbyDevice == null) {
            logger.error("switch master-standby fail --- standbyDevice is null");
            throw new BaseException(StatusCode.ERROR,ErrorCodes.CANNOT_FIND_STANDBY_DEVICE);
        }
        //检查备机是否可用，功能单元是否在线
        List<DeviceNodePO> masterDeviceNodes = deviceNodeDao.findByDeviceId(masterDevice.getId());
        List<DeviceNodePO> standbyDeviceNodes = deviceNodeDao.findByDeviceId(standbyDevice.getId());
        if (standbyDeviceNodes.size()<masterDeviceNodes.size()){
            logger.error("switch master-standby fail --- standby node size less than master");
            throw new BaseException(StatusCode.ERROR,ErrorCodes.STANDBY_DEVICENODE_SIZE_NOTENOUGH);
        }
		// 单节点选
		if (masterDeviceNodes.size()==1){
            DeviceNodePO masterNode = masterDeviceNodes.get(0);
            if (!masterNode.getFunUnitStatus().equals(CommonConstants.FunUnitStatus.NONE)){
                if (!standbyDeviceNodes.get(0).getFunUnitStatus().equals(CommonConstants.FunUnitStatus.NORMAL)){
                    logger.error("switch master-standby fail --- standby device encap fun unit abnormal.");
                    throw new BaseException(StatusCode.FORBIDDEN,ErrorCodes.STANDBY_DEVICE_FUNUNIT_ABNORMAL);
                }
            }

		}else{
			Map<Integer,Integer> funMap = new HashMap<>();//key is position,
			for (int i = 0; i <masterDeviceNodes.size();i++){
				DeviceNodePO curMasterNode = masterDeviceNodes.get(i);
				Integer curPosition = curMasterNode.getPosition();
				Integer value = 0;
				 if (!curMasterNode.getFunUnitStatus().equals(CommonConstants.FunUnitStatus.NONE)){
					value = 1;//有功能单元
				}else{
					value = 0;//没有功能单元
				}
				funMap.put(curPosition,value);
			}
            for (int i = 0; i < standbyDeviceNodes.size(); i++) {
                DeviceNodePO deviceNodePO =	standbyDeviceNodes.get(i);
                Integer fun = funMap.get(deviceNodePO.getPosition());
                if (fun == null) {
                    continue;
                }
                if ((1==fun) && (!deviceNodePO.getFunUnitStatus().equals(CommonConstants.FunUnitStatus.NORMAL))){
                    logger.error("switch master-standby fail --- standby device position {} encap fun unit abnormal.",deviceNodePO.getPosition());
                    throw new BaseException(StatusCode.FORBIDDEN, ErrorCodes.STANDBY_DEVICE_FUNUNIT_ABNORMAL);
                }
            }
		}
// todo 备份操作临时注释       BackupOperate backupOperate =  SpringBeanFactory.getBean(BackupOperate.class); // new的bean 类的成员变量容器不会自动注入 new BackupOperate_YZX();
//        backupOperate.downMasterToStandby(masterDeviceNodes,standbyDeviceNodes,true);
        logger.info("manual switch master-standby end, masterDeviceId: {}, standbyDeviceId: {}", masterDeviceId,standbyDeviceId);
    }

    /**
     * 触发自动备份操作，实际在BackupOperate类的execute方法中执行
     * @param deviceId 主机ID
     * @param autoBackupType 触发设备自动备份的条件类型
     */
    public void triggerAutoBackup(Long deviceId, AutoBackupType autoBackupType){
        if (!beTriggerBackupByConditionOfType(autoBackupType)){
            logger.info("autoBackupType [{}] response to backupCondition cannot trigger auto backup",autoBackupType);
            return;
        }
        autoBackupOperateOfferQueue(deviceId);
    }
    //todo 备份操作临时注释
//    public void triggerAutoBackupWithNetCardError(Long deviceId, NetCardInfoPO netCardInfoPO){
//        BackupConditionPO backupConditionPO = backupConditionDao.findTopByIdIsNotNull();
//        NetGroupPO netGroupPO = netGroupDao.findOne(netCardInfoPO.getNetGroupId());
//        if (netGroupPO == null) {
//            logger.info("trigger autoBackupWithNetCardError fail --- netGroup is null, device [{}] netCardInfo [{}]",deviceId,netCardInfoPO.getId());
//            return;
//        }
//        if (netGroupPO.getNetType().equals(CommonConstants.NetGroupType.INPUT)){
//            if (backupConditionPO.getInputNetCardError().equals(BackupConditionPO.NetCardErrorType.NONE)){
//                logger.info("inputNetCardError cannot trigger auto backup");
//                return;
//            }else if (backupConditionPO.getInputNetCardError().equals(BackupConditionPO.NetCardErrorType.ANY)){
//                logger.info("Any InputNetCardError trigger auto backup");
//                autoBackupOperateOfferQueue(deviceId);
//            }else if (backupConditionPO.getInputNetCardError().equals(BackupConditionPO.NetCardErrorType.ALL)){
//                Boolean beTrigger = true;
//                //查同一设备的已配的网卡
//                List<NetCardInfoPO> netCardInfoPOS = netCardInfoDao.findAllInputNetCardByDeviceId(deviceId);
//                for (NetCardInfoPO netcard : netCardInfoPOS) {
//                    //有一个输入口正常就不备
//                    if (netcard.getStatus()==1){
//                        beTrigger = false;
//                        break;
//                    }
//                }
//                if (beTrigger){
//                    logger.info("All InputNetCardError trigger auto backup");
//                    autoBackupOperateOfferQueue(deviceId);
//                }
//            }else{
//                logger.error("unknown netCardErrorType [{}]",backupConditionPO.getInputNetCardError());
//                return;
//            }
//        }else if (netGroupPO.getNetType().equals(CommonConstants.NetGroupType.OUTPUT)){
//            if (!backupConditionPO.getOutputNetCardError()){
//                logger.info("outputNetCardError cannot trigger auto backup");
//                return;
//            }else{
//                logger.info("outputNetCardError trigger auto backup");
//                autoBackupOperateOfferQueue(deviceId);
//            }
//        }else {
//            // 表示网卡类型是Data，Data既是输入又是输出
//            // 相当于任一网卡异常都直接备(除非入且出异常都不触发备，那就不备，这种情况可能不存在)
//            if (!backupConditionPO.getOutputNetCardError()&&backupConditionPO.getInputNetCardError().equals(BackupConditionPO.NetCardErrorType.NONE)){
//                return;
//            }else{
//                //如果输入输出网卡异常都配了触发备份，就无法区分究竟是输入还是输出网口异常触发的
//                logger.info("Data NetCardError trigger auto backup, because config any input or output can trigger backup");
//                autoBackupOperateOfferQueue(deviceId);
//            }
//        }
//    }
    /**
     * 将自动备份加入操作队列
     * @param deviceId
     */
    public void autoBackupOperateOfferQueue(Long deviceId) {
// todo       totalOptControl.addOpt(SpringBeanFactory.getBean(BackupOperate.class,deviceId,true));
    }
    /**
     * 检查备份类型对应的备份条件 是否可以触发备份
     * @param autoBackupType
     * @return
     */
    private boolean beTriggerBackupByConditionOfType(AutoBackupType autoBackupType) {
        BackupConditionPO backupConditionPO = backupConditionDao.findTopByIdIsNotNull();
        switch (autoBackupType){
            case CPU_OVERRIDE:return backupConditionPO.getCpuOverride();
            case GPU_OVERRIDE:return backupConditionPO.getGpuOverride();
            case CTRL_PORT_DISCONNECT:return backupConditionPO.getCtrlPortDisconnect();
        }
        return false;
    }

    /**
     * 修改配置的自动备份条件
     * @param cpu CPU过载
     * @param gpu GPU过载
     * @param input 输入网卡异常
     * @param output 输出网卡异常
     * @param ctrl 控制口断链
     */
    public void modifyBackupCondition(Boolean cpu, Boolean gpu, BackupConditionPO.NetCardErrorType input, Boolean output, Boolean ctrl){
        BackupConditionPO backupCondition = backupConditionDao.findTopByIdIsNotNull();
        backupCondition.setCpuOverride(cpu);
        backupCondition.setGpuOverride(gpu);
        backupCondition.setInputNetCardError(input);
        backupCondition.setOutputNetCardError(output);
        backupCondition.setCtrlPortDisconnect(ctrl);
        backupConditionDao.save(backupCondition);
    }

    public void modifyBackupCondition(BackupConditionPO backupConditionPO){
        BackupConditionPO backupCondition = backupConditionDao.findTopByIdIsNotNull();
        backupCondition.setInputNetCardError(backupConditionPO.getInputNetCardError());
        backupCondition.setOutputNetCardError(backupConditionPO.getOutputNetCardError());
        backupCondition.setCpuOverride(backupConditionPO.getCpuOverride());
        backupCondition.setGpuOverride(backupConditionPO.getGpuOverride());
        backupCondition.setCtrlPortDisconnect(backupConditionPO.getCtrlPortDisconnect());
        backupConditionDao.save(backupCondition);
    }
    
    public void getBackupCondition(){
        
    }

}
