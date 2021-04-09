package com.sumavision.tetris.bvc.model.agenda;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum DestinationType {

	ROLE("角色"),
	GROUP_MEMBER("会议成员"),
	HALL("会场");
	
	private String name;
	
	private DestinationType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static DestinationType fromName(String name) throws Exception{
		DestinationType[] values = DestinationType.values();
		for(DestinationType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
