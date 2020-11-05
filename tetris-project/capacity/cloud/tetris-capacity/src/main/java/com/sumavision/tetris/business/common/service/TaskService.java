package com.sumavision.tetris.business.common.service;/**
 * Created by Poemafar on 2020/9/18 15:39
 */

import com.sumavision.tetris.business.common.ResultBO;
import com.sumavision.tetris.business.common.Util.IdConstructor;
import com.sumavision.tetris.business.common.Util.NodeUtil;
import com.sumavision.tetris.business.common.enumeration.BusinessType;
import com.sumavision.tetris.business.common.enumeration.ProtocolType;
import com.sumavision.tetris.business.transcode.service.TranscodeTaskService;
import com.sumavision.tetris.business.transcode.vo.TranscodeTaskVO;
import com.sumavision.tetris.capacity.bo.input.InputBO;
import com.sumavision.tetris.capacity.bo.output.OutputBO;
import com.sumavision.tetris.capacity.bo.task.TaskBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: TaskService
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/9/18 15:39
 */
@Service
public class TaskService {

    @Autowired
    NodeUtil nodeUtil;

    @Autowired
    TranscodeTaskService transcodeTaskService;

    /**
     * 转发流，将信源转发到另一地址进行输出
     * @param transModuleIp
     * @param inType
     * @param inUrl
     * @param outType
     * @param outUrl
     */
    public ResultBO transferStream(String transModuleIp,String missionId, ProtocolType inType, String inUrl,String srtMode, ProtocolType outType, String outUrl,BusinessType busType) throws Exception {
        ResultBO resultBO = new ResultBO();

        IdConstructor idCtor = new IdConstructor(missionId);

        List<InputBO> inputBOS = new ArrayList<>();
        List<TaskBO> taskBOS = new ArrayList<>();
        List<OutputBO> outputBOS = new ArrayList<>();
        //拼接输入
        InputBO inputBO = nodeUtil.getPassbyInputInCommand(idCtor,inType,inUrl,srtMode,transModuleIp);

        inputBOS.add(inputBO);
        //任务
        taskBOS = nodeUtil.getPassbyTasksInCommond(idCtor);
        //输出
        OutputBO outputBO = nodeUtil.getPassbyOutputInCommond(idCtor,outType,outUrl,transModuleIp);
        outputBOS.add(outputBO);
        transcodeTaskService.save(idCtor.getJobId(), transModuleIp, inputBOS, taskBOS, outputBOS, busType);
        resultBO.setMissionId(idCtor.getJobId());
        return resultBO;
    }


}


