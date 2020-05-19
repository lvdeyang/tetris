package com.sumavision.tetris.websocket.core;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum CustomProperties {

	CLIENT_IP("客户端ip");
	
	private String name;
	
	private CustomProperties(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static CustomProperties fromName(String name) throws Exception{
		CustomProperties[] values = CustomProperties.values();
		for(CustomProperties value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
