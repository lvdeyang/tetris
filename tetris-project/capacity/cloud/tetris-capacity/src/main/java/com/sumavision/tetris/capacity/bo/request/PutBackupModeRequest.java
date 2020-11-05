package com.sumavision.tetris.capacity.bo.request;/**
 * Created by Poemafar on 2020/8/18 10:34
 */

/**
 * @ClassName: PutBackupModeRequest
 * @Description TODO
 * @Author: Poemafar
 * @Version：1.0
 * @Date 2020/8/18 10:34
 */
public class PutBackupModeRequest {

    private String msg_id;

    private String inputId;

    private String mode;

    private String select_index;

    public String getMsg_id() {
        return msg_id;
    }

    public void setMsg_id(String msg_id) {
        this.msg_id = msg_id;
    }

    public String getInputId() {
        return inputId;
    }

    public void setInputId(String inputId) {
        this.inputId = inputId;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getSelect_index() {
        return select_index;
    }

    public void setSelect_index(String select_index) {
        this.select_index = select_index;
    }
}
