package com.sumavision.tetris.sts.task.taskParamOutput;

import java.io.Serializable;

public class PassbyTsHttpBO extends PassbyCommonBO implements OutputCommon,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4287346100040244726L;
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

}
