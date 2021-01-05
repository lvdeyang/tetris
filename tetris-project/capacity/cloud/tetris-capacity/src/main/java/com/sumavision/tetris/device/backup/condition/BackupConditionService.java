package com.sumavision.tetris.device.backup.condition;/**
 * Created by Poemafar on 2020/12/29 10:55
 */

import com.sumavision.tetris.device.netcard.NetCardInfoDao;
import com.sumavision.tetris.device.netcard.NetCardInfoPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName: BackupConditionService
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/12/29 10:55
 */
@Service
public class BackupConditionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BackupConditionService.class);

    @Autowired
    NetCardInfoDao netCardInfoDao;

    @Autowired
    BackupConditionDao backupConditionDao;

    /**
     * 修改配置的自动备份条件
     * @param cpu CPU过载
     * @param gpu GPU过载
     * @param input 输入网卡异常
     * @param output 输出网卡异常
     * @param ctrl 控制口断链
     */
    public BackupConditionPO modifyBackupCondition(Boolean cpu, Boolean gpu, BackupConditionPO.NetCardErrorType input, Boolean output, Boolean ctrl){
        BackupConditionPO backupCondition = backupConditionDao.findTopByIdIsNotNull();
        backupCondition.setCpuOverride(cpu);
        backupCondition.setGpuOverride(gpu);
        backupCondition.setInputNetCardError(input);
        backupCondition.setOutputNetCardError(output);
        backupCondition.setCtrlPortDisconnect(ctrl);
        backupConditionDao.save(backupCondition);
        return backupCondition;
    }

    public Boolean checkAutoBackupByCtrlPort(){
        BackupConditionPO backupConditionPO = backupConditionDao.findTopByIdIsNotNull();
        if (backupConditionPO==null){
            LOGGER.warn("获取自动备份条件失败");
            return false;
        }
        return backupConditionPO.getCtrlPortDisconnect();
    }

    public Boolean checkAutoBackupByNetCardError(List<NetCardInfoPO> nets){
        if (nets==null || nets.isEmpty()){
            return false;
        }
        BackupConditionPO backupConditionPO = backupConditionDao.findTopByIdIsNotNull();
        if (backupConditionPO==null){
            LOGGER.warn("获取自动备份条件失败");
            return false;
        }
        if (!backupConditionPO.getOutputNetCardError()){
            LOGGER.info("outputNetCardError cannot trigger auto backup");
            return false;
        }else{
            for (int i = 0; i < nets.size(); i++) {
                NetCardInfoPO net = nets.get(i);
                if (net.getOutputNetGroupId()!=null){
                    LOGGER.info("output net card error trigger auto backup");
                    return true;
                }
            }
        }

        if (BackupConditionPO.NetCardErrorType.NONE.equals(backupConditionPO.getInputNetCardError())){
            LOGGER.info("inputNetCardError cannot trigger auto backup");
            return false;
        }else if (BackupConditionPO.NetCardErrorType.ALL.equals(backupConditionPO.getInputNetCardError())){ //全异常才切换
            Integer normalNum = netCardInfoDao.countInputNetCardsByDeviceIdAndStatus(nets.get(0).getDeviceId(),1);
            if (normalNum > 0) {
                return false;
            }else{
                LOGGER.info("all input net card error trigger auto backup");
                return true;
            }
        }else if (BackupConditionPO.NetCardErrorType.ANY.equals(backupConditionPO.getInputNetCardError())){
            for (int i = 0; i < nets.size(); i++) {
                NetCardInfoPO net = nets.get(i);
                if (net.getInputNetGroupId()!=null){
                    LOGGER.info("any input net card error trigger auto backup");
                    return true;
                }
            }
        }else {
            LOGGER.info("unknown inputNetCardError type: {}",backupConditionPO.getInputNetCardError());
            return false;
        }
        return false;
    }
}
