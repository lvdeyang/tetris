package com.sumavision.tetris.capacity.bo.response;

import com.sumavision.tetris.capacity.bo.task.TaskBO;

import java.util.List;

/**
 * Created by Poemafar on 2020/6/23 10:23
 */
public class PlatformResponse {

    private String msg_id;

    private Integer result_code;

    private List<String> platform_array;

    private String platform;

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public Integer getResult_code() {
        return result_code;
    }

    public void setResult_code(Integer result_code) {
        this.result_code = result_code;
    }

    public List<String> getPlatform_array() {
        return platform_array;
    }

    public void setPlatform_array(List<String> platform_array) {
        this.platform_array = platform_array;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
