package com.sumavision.tetris.sts.task.createNode;

import java.util.ArrayList;

import com.sumavision.tetris.sts.common.IdConstructor;
import com.sumavision.tetris.sts.task.taskParamInput.InputNode;


public class CreateJsonInputNode {
	private String msg_id;
	private ArrayList<InputNode> input_array;
	
	public CreateJsonInputNode() {
        this.msg_id = String.valueOf(IdConstructor.getMsgId()) ;
    }
	public String getMsg_id() {
		return msg_id;
	}
	public void setMsg_id(String msg_id) {
		this.msg_id = msg_id;
	}
	public ArrayList<InputNode> getInput_array() {
		return input_array;
	}
	public void setInput_array(ArrayList<InputNode> input_array) {
		this.input_array = input_array;
	}
	
}
