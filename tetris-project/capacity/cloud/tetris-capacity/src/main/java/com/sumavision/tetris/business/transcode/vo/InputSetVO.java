package com.sumavision.tetris.business.transcode.vo;

import com.sumavision.tetris.capacity.bo.request.*;

import java.io.Serializable;
import java.util.ArrayList;

public class InputSetVO implements Serializable {
    private static final long serialVersionUID = 6230258714652958134L;

    private String device_ip;

    private ArrayList<PutElementsRequest> modify_program_param;

    private ArrayList<PutBackupModeRequest> modify_backup_mode;


    public String getDevice_ip() {
        return device_ip;
    }

    public void setDevice_ip(String device_ip) {
        this.device_ip = device_ip;
    }

    public ArrayList<PutElementsRequest> getModify_program_param() {
        return modify_program_param;
    }

    public void setModify_program_param(ArrayList<PutElementsRequest> modify_program_param) {
        this.modify_program_param = modify_program_param;
    }

    public ArrayList<PutBackupModeRequest> getModify_backup_mode() {
        return modify_backup_mode;
    }

    public void setModify_backup_mode(ArrayList<PutBackupModeRequest> modify_backup_mode) {
        this.modify_backup_mode = modify_backup_mode;
    }
}
