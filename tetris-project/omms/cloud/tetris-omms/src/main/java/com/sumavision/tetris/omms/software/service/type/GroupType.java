package com.sumavision.tetris.omms.software.service.type;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum GroupType {

	SPRING_CLOUD("微服务"),
	PROTOCOL_CONVERSION("协议转换服务");
	
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
