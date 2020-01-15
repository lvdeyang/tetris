package com.sumavision.tetris.sts.task.createNode;

import java.util.ArrayList;

import com.sumavision.tetris.sts.common.IdConstructor;
import com.sumavision.tetris.sts.task.taskParamInput.InputProgramBO;


public class CreateJsonProgramNode {
	private String msg_id;
	private ArrayList<InputProgramBO> program_array;
	
	public CreateJsonProgramNode() {
        this.msg_id = String.valueOf(IdConstructor.getMsgId()) ;
    }
	public String getMsg_id() {
		return msg_id;
	}
	public void setMsg_id(String msg_id) {
		this.msg_id = msg_id;
	}
	public ArrayList<InputProgramBO> getProgram_array() {
		return program_array;
	}
	public void setProgram_array(ArrayList<InputProgramBO> program_array) {
		this.program_array = program_array;
	}
	
}
