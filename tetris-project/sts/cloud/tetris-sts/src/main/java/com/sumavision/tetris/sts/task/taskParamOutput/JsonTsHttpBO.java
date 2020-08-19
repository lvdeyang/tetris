package com.sumavision.tetris.sts.task.taskParamOutput;

import java.io.Serializable;
import java.util.ArrayList;

public class JsonTsHttpBO  extends JsonTsCommonBO implements  OutputCommon,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7646938121266259786L;
	
	private String name;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	

}
