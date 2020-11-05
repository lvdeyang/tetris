package com.sumavision.tetris.bvc.model.agenda;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum AgendaModeType {

	LOOP("轮询"),
	COMBINE_VIDEO("合屏");
	
	private String name;
	
	private AgendaModeType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static AgendaModeType fromName(String name) throws Exception{
		AgendaModeType[] values = AgendaModeType.values();
		for(AgendaModeType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
