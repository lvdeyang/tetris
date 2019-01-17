package com.sumavision.tetris.easy.process.core;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum ProcessType {

	/** 模板流程不可发布 */
	TEMPLATE("流程模板"),
	
	/**需要手动发布的流程 */
	PUBLISH("可发布的");
	
	private String name;
	
	private ProcessType(String name) {
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static ProcessType fromName(String name) throws Exception{
		ProcessType[] values = ProcessType.values();
		for(ProcessType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
