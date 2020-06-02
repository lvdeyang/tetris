package com.sumavision.tetris.bvc.business.group;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum BusinessType {

	COMMAND("指挥业务"),
	MEETING_QT("会议业务"),
	MEETING_BVC("会控业务");
	
	private String name;
	
	private BusinessType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static BusinessType fromName(String name) throws Exception{
		BusinessType[] values = BusinessType.values();
		for(BusinessType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
