package com.suma.xianrd.sirius.jsonbo.node;

import java.util.ArrayList;

import com.suma.xianrd.sirius.common.IdConstructor;
import com.suma.xianrd.sirius.jsonbo.source.CreateInputProgramBO;

public class DelJsonProgramNode {
	private String msg_id;
	private ArrayList<CreateInputProgramBO> program_array;
	
	public DelJsonProgramNode() {
		this.msg_id = String.valueOf(IdConstructor.getMsgId()) ;
	}
	public String getMsg_id() {
		return msg_id;
	}
	public void setMsg_id(String msg_id) {
		this.msg_id = msg_id;
	}
	public ArrayList<CreateInputProgramBO> getProgram_array() {
		return program_array;
	}
	public void setProgram_array(ArrayList<CreateInputProgramBO> program_array) {
		this.program_array = program_array;
	}
	
	
}
