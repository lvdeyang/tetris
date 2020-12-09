package com.sumavision.tetris.business.transcode.vo;/**
 * Created by Poemafar on 2020/8/31 16:21
 */

import com.sumavision.tetris.capacity.bo.input.InputBO;

import java.util.List;

/**
 * @ClassName: AnalysisStreamVO
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/8/31 16:21
 */
public class AnalysisStreamVO {

    private String deviceIp;

    private String msg_id;

    private String type;

    private String inputId;

    private List<InputBO> input_array;

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getInputId() {
        return inputId;
    }

    public void setInputId(String inputId) {
        this.inputId = inputId;
    }

    public List<InputBO> getInput_array() {
        return input_array;
    }

    public void setInput_array(List<InputBO> input_array) {
        this.input_array = input_array;
    }

    public String getDeviceIp() {
        return deviceIp;
    }

    public void setDeviceIp(String deviceIp) {
        this.deviceIp = deviceIp;
    }
}
