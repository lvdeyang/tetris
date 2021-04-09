package com.sumavision.bvc.system.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum ServLevel {

	LEVEL_ONE("一级栏目"),
	LEVEL_TWO("会议类型"),
	DEFAULT("默认");
	
	private String name;
	
	private ServLevel(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static ServLevel fromName(String name) throws Exception{
		ServLevel [] values = ServLevel.values();
		for(ServLevel value : values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("ServLevel", name);
	}
	
	
	
}
