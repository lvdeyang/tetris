package com.sumavision.tetris.omms.software.service.installation;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum PropertyValueType {

	TEXT("文本"),
	BOOLEAN("布尔值"),
	ENUM("枚举"),
	IP("ip地址"),
	DBIP("数据库IP"),
	DBPORT("数据库端口");
	
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
