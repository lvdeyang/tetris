package com.sumavision.tetris.business.transcode.vo;/**
 * Created by Poemafar on 2020/9/1 13:51
 */

import com.sumavision.tetris.business.bvc.vo.BvcAudioParamVO;
import com.sumavision.tetris.business.bvc.vo.BvcVideoParamVO;
import com.sumavision.tetris.capacity.bo.input.InputBO;

import java.util.List;

/**
 * @ClassName: CreateInputsVO
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/9/1 13:51
 */
public class CreateInputPreviewVO {

    private String msg_id;

    private String device_ip;

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
}
