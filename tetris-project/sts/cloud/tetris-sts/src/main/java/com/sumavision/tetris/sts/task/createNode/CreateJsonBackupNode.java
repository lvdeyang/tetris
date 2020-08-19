package com.sumavision.tetris.sts.task.createNode;

import com.sumavision.tetris.sts.common.IdConstructor;
import com.sumavision.tetris.sts.task.taskParamInput.BackupBO;


public class CreateJsonBackupNode {
	private String msg_id;
	private BackupBO back_up;
	
	public CreateJsonBackupNode() {
        this.msg_id = String.valueOf(IdConstructor.getMsgId()) ;
    }
	public String getMsg_id() {
		return msg_id;
	}
	public void setMsg_id(String msg_id) {
		this.msg_id = msg_id;
	}
	public BackupBO getBack_up() {
		return back_up;
	}
	public void setBack_up(BackupBO back_up) {
		this.back_up = back_up;
	}
	
}
