package com.sumavision.signal.bvc.capacity.bo;/**
 * Created by Poemafar on 2020/9/24 15:14
 */

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sumavision.signal.bvc.capacity.bo.output.BaseTsOutputBO;
import com.sumavision.signal.bvc.capacity.bo.output.OutputBO;
import com.sumavision.signal.bvc.capacity.bo.output.OutputProgramBO;
import com.sumavision.signal.bvc.common.IdConstructor;
import com.sumavision.signal.bvc.common.enumeration.CommonConstants.*;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import org.assertj.core.util.Strings;

import javax.jws.WebParam;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName: MissionBO
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/9/24 15:14
 */
public class MissionBO {

    IdConstructor idCtor = new IdConstructor();

    String device_ip;

    List input_array;

    List task_array;

    List output_array;

    TaskType taskType;

    SwitchMode switchMode;

    String inUrl;

    String outUrl;

    ProtocolType inType;

    ProtocolType outType;

    String srtMode;

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

    public List getInput_array() {
        return input_array;
    }

    public void setInput_array(List input_array) {
        this.input_array = input_array;
    }

    public List getTask_array() {
        return task_array;
    }

    public void setTask_array(List task_array) {
        this.task_array = task_array;
    }

    public List getOutput_array() {
        return output_array;
    }

    public void setOutput_array(List output_array) {
        this.output_array = output_array;
    }

    public String getInUrl() {
        return inUrl;
    }

    public void setInUrl(String inUrl) {
        this.inUrl = inUrl;
    }

    public String getOutUrl() {
        return outUrl;
    }

    public void setOutUrl(String outUrl) {
        this.outUrl = outUrl;
    }

    public ProtocolType getInType() {
        return inType;
    }

    public void setInType(ProtocolType inType) {
        this.inType = inType;
    }

    public ProtocolType getOutType() {
        return outType;
    }

    public void setOutType(ProtocolType outType) {
        this.outType = outType;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public SwitchMode getSwitchMode() {
        return switchMode;
    }

    public void setSwitchMode(SwitchMode switchMode) {
        this.switchMode = switchMode;
    }

    public String getSrtMode() {
        return srtMode;
    }

    public void setSrtMode(String srtMode) {
        this.srtMode = srtMode;
    }
}
