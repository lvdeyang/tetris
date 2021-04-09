package com.sumavision.tetris.bvc.business.group.forward;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum ForwardBusinessType {

	QT_TOTAL_FORWARD("qt全部上屏");
	
	private String name;
	
	private ForwardBusinessType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static ForwardBusinessType fromName(String name) throws Exception{
		ForwardBusinessType[] values = ForwardBusinessType.values();
		for(ForwardBusinessType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
