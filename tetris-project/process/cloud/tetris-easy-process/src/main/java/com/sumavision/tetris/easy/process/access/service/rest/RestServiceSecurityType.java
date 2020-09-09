package com.sumavision.tetris.easy.process.access.service.rest;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum RestServiceSecurityType {

	PARAM("参数"),
	HEADER("请求头");
	
	private String name;
	
	private RestServiceSecurityType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static RestServiceSecurityType fromName(String name) throws Exception{
		RestServiceSecurityType[] values = RestServiceSecurityType.values();
		for(RestServiceSecurityType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
