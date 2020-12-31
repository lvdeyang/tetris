package com.sumavision.tetris.business.common;/**
 * Created by Poemafar on 2020/11/4 16:00
 */

import com.sumavision.tetris.application.template.TemplatePO.*;
import com.sumavision.tetris.business.common.Util.IdConstructor;
import com.sumavision.tetris.business.common.enumeration.MediaType;
import com.sumavision.tetris.business.common.enumeration.TaskType;
import com.sumavision.tetris.capacity.bo.input.InputBO;
import com.sumavision.tetris.capacity.bo.output.OutputBO;
import com.sumavision.tetris.capacity.bo.task.TaskBO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: MissionBO
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/11/4 16:00
 */
public class MissionBO {

    IdConstructor idCtor;

    String device_ip;

    TaskType taskType;

//    List<InputBO> input_array = new ArrayList();

    List<TaskBO> task_array = new ArrayList();

    List<OutputBO> output_array = new ArrayList();

    /**
     * 输入索引号和对应的INPUTBO
     */
    Map<Integer,InputBO> inputMap = new HashMap<>();


    /**
     * 用于记录templatevo输出 index 和 转换参数 编码ID的映射关系
     */
    Map<Integer,String> outEncodeMap = new HashMap<>();

    Map<Integer, String> mediaTypeMap = new HashMap<>();

    public IdConstructor getIdCtor() {
        return idCtor;
    }

    public void setIdCtor(IdConstructor idCtor) {
        this.idCtor = idCtor;
    }

    public String getDevice_ip() {
        return device_ip;
    }

    public void setDevice_ip(String device_ip) {
        this.device_ip = device_ip;
    }

//    public List<InputBO> getInput_array() {
//        return input_array;
//    }
//
//    public void setInput_array(List<InputBO> input_array) {
//        this.input_array = input_array;
//    }

    public List<TaskBO> getTask_array() {
        return task_array;
    }

    public void setTask_array(List<TaskBO> task_array) {
        this.task_array = task_array;
    }

    public List<OutputBO> getOutput_array() {
        return output_array;
    }

    public void setOutput_array(List<OutputBO> output_array) {
        this.output_array = output_array;
    }

    public Map<Integer, String> getOutEncodeMap() {
        return outEncodeMap;
    }

    public void setOutEncodeMap(Map<Integer, String> outEncodeMap) {
        this.outEncodeMap = outEncodeMap;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public Map<Integer, String> getMediaTypeMap() {
        return mediaTypeMap;
    }

    public void setMediaTypeMap(Map<Integer, String> mediaTypeMap) {
        this.mediaTypeMap = mediaTypeMap;
    }

    public Map<Integer, InputBO> getInputMap() {
        return inputMap;
    }

    public void setInputMap(Map<Integer, InputBO> inputMap) {
        this.inputMap = inputMap;
    }
}
