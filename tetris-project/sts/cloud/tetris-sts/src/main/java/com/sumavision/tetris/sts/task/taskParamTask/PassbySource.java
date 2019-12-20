package com.sumavision.tetris.sts.task.taskParamTask;

import java.io.Serializable;

public class PassbySource implements SourceCommon, Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2856717410361418003L;
	
	private String input_id;

	public PassbySource(String input_id) {
		super();
		this.input_id = input_id;
	}

	public String getInput_id() {
		return input_id;
	}

	public void setInput_id(String input_id) {
		this.input_id = input_id;
	}
	

}
