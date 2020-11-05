package com.suma.xianrd.sirius.jsonbo.node;

import java.util.ArrayList;

import com.suma.xianrd.sirius.common.IdConstructor;
import com.suma.xianrd.sirius.jsonbo.output.OutputNode;

public class DelJsonOutputNode {
	private String msg_id;
	private ArrayList<OutputNode> output_array;
	public DelJsonOutputNode(){
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
