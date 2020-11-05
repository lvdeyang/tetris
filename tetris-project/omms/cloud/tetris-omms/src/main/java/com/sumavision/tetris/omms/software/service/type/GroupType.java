package com.sumavision.tetris.omms.software.service.type;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum GroupType {

	JAVA_SERVER("Java服务"),
	C_SERVER("C服务");
	
	private String name;
	
	private GroupType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static GroupType fromName(String name) throws Exception{
		GroupType[] values = GroupType.values();
		for(GroupType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
