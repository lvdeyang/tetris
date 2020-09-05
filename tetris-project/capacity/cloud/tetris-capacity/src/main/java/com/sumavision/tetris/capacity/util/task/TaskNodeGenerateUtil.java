package com.sumavision.tetris.capacity.util.task;/**
 * Created by Poemafar on 2020/9/4 14:33
 */

import com.sumavision.tetris.capacity.bo.input.InputBO;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import org.springframework.stereotype.Component;

/**
 * @ClassName: TaskNodeGenerateUtil
 * @Description TODO 用于自动生成任务各节点
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/9/4 14:33
 */
@Component
public class TaskNodeGenerateUtil {



    public InputBO generateInputBOByProgram(String taskId, String deviceIp){
        InputBO inputBO = new InputBO();
        String inputId = new StringBufferWrapper().append("input-")
                .append(taskId)
                .toString();





        return inputBO;
    }

}
