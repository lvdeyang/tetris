package com.sumavision.tetris.bvc.model.layout;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum LayoutPositionType {

	REMOTE("远端"),
	LOCAL_1("本地1"),
	LOCAL_2("本地2"),
	LOCAL_3("本地3"),
	LOCAL_4("本地4");
	
	private String name;
	
	private LayoutPositionType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static LayoutPositionType fromName(String name) throws Exception{
		LayoutPositionType[] values = LayoutPositionType.values();
		for(LayoutPositionType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
