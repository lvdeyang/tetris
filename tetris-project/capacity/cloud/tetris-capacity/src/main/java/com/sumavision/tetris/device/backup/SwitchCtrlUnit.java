package com.sumavision.tetris.device.backup;/**
 * Created by Poemafar on 2020/12/25 17:10
 */

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.tetris.business.common.Util.CommonUtil;
import com.sumavision.tetris.business.common.dao.TaskInputDAO;
import com.sumavision.tetris.business.common.dao.TaskOutputDAO;
import com.sumavision.tetris.business.common.enumeration.SwitchMode;
import com.sumavision.tetris.business.common.po.TaskInputPO;
import com.sumavision.tetris.business.common.po.TaskOutputPO;
import com.sumavision.tetris.business.common.service.SyncService;
import com.sumavision.tetris.business.common.service.TaskService;
import com.sumavision.tetris.capacity.bo.input.InputBO;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.device.DeviceDao;
import com.sumavision.tetris.device.DevicePO;
import com.sumavision.tetris.device.netcard.NetCardInfoDao;
import com.sumavision.tetris.device.netcard.NetCardInfoPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @ClassName: BackupService
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/12/25 17:10
 */
@Component
@Transactional(rollbackFor = Exception.class)
public class SwitchCtrlUnit {

    @Autowired
    TaskOutputDAO taskOutputDAO;

    @Autowired
    TaskInputDAO taskInputDAO;

    @Autowired
    TaskService taskService;

    @Autowired
    DeviceDao deviceDao;

    @Autowired
    SyncService syncService;

    @Autowired
    NetCardInfoDao netCardInfoDao;


    public void switchDeviceInDB(DevicePO mainDev, DevicePO backDev) throws Exception {
        List<TaskOutputPO> switchTasks = new ArrayList<>();
        switchTasks = taskOutputDAO.findByCapacityIp(mainDev.getDeviceIp());

//修改收流网口和出流网口
        Map<String,String> inputIpMap = new HashMap<>();
        Map<String,String> outputIpMap = new HashMap<>();

        List<NetCardInfoPO> srcInputNetCards = netCardInfoDao.findInputNetByDeviceId(mainDev.getId());
        for (int i = 0; i < srcInputNetCards.size(); i++) {
            NetCardInfoPO srcNetCard = srcInputNetCards.get(i);
            NetCardInfoPO tgtNetCard = netCardInfoDao.findTopByInputNetGroupIdAndDeviceIdAndStatus(srcNetCard.getInputNetGroupId(), backDev.getId(), 1);
            if (tgtNetCard==null){
                throw new BaseException(StatusCode.FORBIDDEN,"无法切换: 目标设备输入网卡异常");
            }
            inputIpMap.put(srcNetCard.getIpv4(),tgtNetCard.getIpv4());
        }

        List<NetCardInfoPO> srcOutputNetCards = netCardInfoDao.findOutputNetByDeviceId(mainDev.getId());
        for (int i = 0; i < srcOutputNetCards.size(); i++) {
            NetCardInfoPO srcNetCard = srcOutputNetCards.get(i);
            NetCardInfoPO tgtNetCard = netCardInfoDao.findTopByOutputNetGroupIdAndDeviceIdAndStatus(srcNetCard.getOutputNetGroupId(), backDev.getId(), 1);
            if (tgtNetCard==null){
                throw new BaseException(StatusCode.FORBIDDEN,"无法切换: 目标设备输出网卡异常");
            }
            outputIpMap.put(srcNetCard.getIpv4(),tgtNetCard.getIpv4());
        }
//修改任务
        switchTasks.stream().forEach(t->{
            switchTaskInDB(t,inputIpMap,outputIpMap);
        });

        String mainDevIp = mainDev.getDeviceIp();
        String backDevIp = backDev.getDeviceIp();

        taskOutputDAO.updateCapacityIpByIp(mainDevIp,backDevIp);
        taskInputDAO.updateCapacityIpByIp(mainDevIp,backDevIp);

//修改主备状态
        deviceDao.updateBackTypeById(mainDev.getId(),backDev.getBackType());
        deviceDao.updateBackTypeById(backDev.getId(),mainDev.getBackType());

    }

    public void switchTaskInDB(TaskOutputPO output,Map inIpMap,Map outIpMap){
        Set<Long> inputIdSet = new HashSet<>();
        if (output.getInputId()!=null){
            inputIdSet.add(output.getInputId());
        }
        if (output.getInputList()!=null && !output.getInputList().isEmpty()) {
            inputIdSet.addAll(JSONArray.parseArray(output.getInputList(), Long.class));
        }
        List<TaskInputPO> inputs = new ArrayList<>();
        if (!inputIdSet.isEmpty()) {
            inputs = taskInputDAO.findByIdIn(inputIdSet);
        }
        for (int i = 0; i < inputs.size(); i++) {
            TaskInputPO curInput = inputs.get(i);
            String inputStr = curInput.getInput();
            if (!inputStr.contains("local_ip")){
                continue;
            }
            //获取localip修改localip
            JSONObject inputObj = JSONObject.parseObject(inputStr);
            CommonUtil.setValueByKeyFromJson(inputObj,"local_ip",inIpMap);
            String uniq = taskService.generateUniq(JSONObject.parseObject(inputObj.toJSONString(), InputBO.class));
            curInput.setInput(inputObj.toJSONString());
            curInput.setUpdateTime(new Date());
            curInput.setUniq(uniq);
            taskInputDAO.save(curInput);
        }

        String outputStr = output.getOutput();
        JSONArray outputObjs = JSONArray.parseArray(outputStr);
        CommonUtil.setValueByKeyFromJson(outputObjs,"local_ip",outIpMap);
        output.setOutput(outputObjs.toJSONString());
        taskOutputDAO.save(output);
    }


}
