package com.suma.xianrd.sirius.jsonbo.node;

import java.util.ArrayList;

import com.suma.xianrd.sirius.common.IdConstructor;
import com.suma.xianrd.sirius.jsonbo.input.InputNode;

public class DelJsonInputNode {
	private String msg_id;
	private ArrayList<InputNode> input_array;
	
	public DelJsonInputNode() {
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
