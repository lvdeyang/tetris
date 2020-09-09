package com.sumavision.tetris.zoom;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum ZoomMemberType {

	TERMINl("普通终端"),
	JV220("jv220");
	
	private String name;
	
	private ZoomMemberType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static ZoomMemberType fromName(String name) throws Exception{
		ZoomMemberType[] values = ZoomMemberType.values();
		for(ZoomMemberType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}	
		throw new ErrorTypeException("name", name);
	}
	
}
