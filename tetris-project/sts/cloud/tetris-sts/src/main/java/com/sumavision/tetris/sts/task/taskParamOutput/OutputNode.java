package com.sumavision.tetris.sts.task.taskParamOutput;

import java.io.Serializable;


public class OutputNode implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3892607257754125927L;
	private String id;
	private OutputCommon outputCommon;
	private Integer result_code;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public OutputCommon getOutputCommon() {
		return outputCommon;
	}
	public void setOutputCommon(OutputCommon outputCommon) {
		this.outputCommon = outputCommon;
	}
	public Integer getResult_code() {
		return result_code;
	}
	public void setResult_code(Integer result_code) {
		this.result_code = result_code;
	}
	
	
}
