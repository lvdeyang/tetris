package com.sumavision.tetris.omms.software.service.type;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum PropertyValueType {

	TEXT("文本"),
	IP("ip地址"),
	PORT("端口"),
	PATH("路径"),
	URL("url");
	
	private String name;
	
	private PropertyValueType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static PropertyValueType fromName(String name) throws Exception{
		PropertyValueType[] values = PropertyValueType.values();
		for(PropertyValueType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
