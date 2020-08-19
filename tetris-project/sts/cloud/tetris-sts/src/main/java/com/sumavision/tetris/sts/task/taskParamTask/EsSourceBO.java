package com.sumavision.tetris.sts.task.taskParamTask;

import java.io.Serializable;

public class EsSourceBO implements SourceCommon, Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8534192289014545962L;
	private String input_id;
	private Integer program_number;
	private Integer element_pid;
	
	public EsSourceBO(String input_id, Integer program_number, Integer element_pid){
		this.input_id = input_id;
		this.program_number = program_number;
		this.element_pid = element_pid;
	}
	public String getInput_id() {
		return input_id;
	}
	public void setInput_id(String input_id) {
		this.input_id = input_id;
	}
	public Integer getProgram_number() {
		return program_number;
	}
	public void setProgram_number(Integer program_number) {
		this.program_number = program_number;
	}
	public Integer getElement_pid() {
		return element_pid;
	}
	public void setElement_pid(Integer element_pid) {
		this.element_pid = element_pid;
	}
	
	

}
