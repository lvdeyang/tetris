package com.sumavision.tetris.bvc.business.group.forward;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum Jv230ForwardBusinessType {

	QT_TOTAL_FORWARD("qt全部上屏");
	
	private String name;
	
	private Jv230ForwardBusinessType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static Jv230ForwardBusinessType fromName(String name) throws Exception{
		Jv230ForwardBusinessType[] values = Jv230ForwardBusinessType.values();
		for(Jv230ForwardBusinessType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
