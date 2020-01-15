package com.sumavision.tetris.sts.task.taskParamInput;

import java.io.Serializable;
import java.util.ArrayList;

public class BackupBO implements InputCommon,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2942332518309746517L;
	private String mode;
	private Integer select_index;
	private ArrayList<InputProgramBO> program_array;
	public String getMode() {
		return mode;
	}
	public void setMode(String mode) {
		this.mode = mode;
	}
	public Integer getSelect_index() {
		return select_index;
	}
	public void setSelect_index(Integer select_index) {
		this.select_index = select_index;
	}
	public ArrayList<InputProgramBO> getProgram_array() {
		return program_array;
	}
	public void setProgram_array(ArrayList<InputProgramBO> program_array) {
		this.program_array = program_array;
	}
	
}
