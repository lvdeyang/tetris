package com.suma.xianrd.sirius.jsonbo.node;

import java.util.ArrayList;

import com.suma.xianrd.sirius.common.IdConstructor;
import com.suma.xianrd.sirius.jsonbo.task.TaskNode;

public class CreateJsonTaskNode {
	private String msg_id;
	private ArrayList<TaskNode> task_array;
	
	public CreateJsonTaskNode() {
        this.msg_id = String.valueOf(IdConstructor.getMsgId()) ;
    }
	

	public String getMsg_id() {
		return msg_id;
	}

	public void setMsg_id(String msg_id) {
		this.msg_id = msg_id;
	}

	public ArrayList<TaskNode> getTask_array() {
		return task_array;
	}

	public void setTask_array(ArrayList<TaskNode> task_array) {
		this.task_array = task_array;
	}
	
	
	
}
