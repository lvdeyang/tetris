package com.sumavision.tetris.bvc.business.group;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum BusinessOrderGroupType {
	COMMON_ORDER("预约会议");
	
	private String name;
	
	private BusinessOrderGroupType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static BusinessOrderGroupType fromName(String name) throws Exception{
		BusinessOrderGroupType[] values = BusinessOrderGroupType.values();
		for(BusinessOrderGroupType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
