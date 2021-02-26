package com.sumavision.tetris.business.transcode.vo;/**
 * Created by Poemafar on 2020/9/1 13:51
 */

import com.sumavision.tetris.capacity.bo.input.InputBO;
import com.sumavision.tetris.capacity.constant.Constant;

import java.util.List;

/**
 * @ClassName: CreateInputsVO
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/9/1 13:51
 */
public class InputPreviewVO {
    /**
     * 操作类型：create添加预览  delete 删除预览
     */
    private String operate;

    /**
     * 删除预览任务的ID
     */
    private String delInputId;


    private String msg_id;

    /**
     * 预览任务的设备
     */
    private String device_ip;

    private Integer device_port= Constant.TRANSFORM_PORT;

    /**
     * 收流的设备,如果是null说明是任意设备都能收到流
     */
    private String receive_stream_device;

    private Integer program_number;
    /**
     * 其他业务创建输入
     */
    private List<InputBO> input_array;

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getDevice_ip() {
        return device_ip;
    }

    public void setDevice_ip(String device_ip) {
        this.device_ip = device_ip;
    }

    public List<InputBO> getInput_array() {
        return input_array;
    }

    public void setInput_array(List<InputBO> input_array) {
        this.input_array = input_array;
    }

    public Integer getProgram_number() {
        return program_number;
    }

    public void setProgram_number(Integer program_number) {
        this.program_number = program_number;
    }

    public String getReceive_stream_device() {
        return receive_stream_device;
    }

    public InputPreviewVO setReceive_stream_device(String receive_stream_device) {
        this.receive_stream_device = receive_stream_device;
        return this;
    }

    public String getOperate() {
        return operate;
    }

    public InputPreviewVO setOperate(String operate) {
        this.operate = operate;
        return this;
    }

    public String getDelInputId() {
        return delInputId;
    }

    public InputPreviewVO setDelInputId(String delInputId) {
        this.delInputId = delInputId;
        return this;
    }

    public Integer getDevice_port() {
        return device_port;
    }

    public InputPreviewVO setDevice_port(Integer device_port) {
        this.device_port = device_port;
        return this;
    }
}
