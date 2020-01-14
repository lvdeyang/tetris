package com.suma.xianrd.sirius.jsonbo.node;


import com.suma.xianrd.sirius.common.IdConstructor;
import com.suma.xianrd.sirius.jsonbo.input.BackupBO;

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
