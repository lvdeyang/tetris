package com.sumavision.tetris.sts.task.createNode;

import java.util.ArrayList;

import com.sumavision.tetris.sts.common.IdConstructor;
import com.sumavision.tetris.sts.task.taskParamOutput.OutputNode;


public class CreateJsonOutputNode {
	private String msg_id;
	private ArrayList<OutputNode> output_array;
	public CreateJsonOutputNode(){
		this.msg_id = String.valueOf(IdConstructor.getMsgId()) ;
	}
	public String getMsg_id() {
		return msg_id;
	}
	public void setMsg_id(String msg_id) {
		this.msg_id = msg_id;
	}
	public ArrayList<OutputNode> getOutput_array() {
		return output_array;
	}
	public void setOutput_array(ArrayList<OutputNode> output_array) {
		this.output_array = output_array;
	}
	
}
