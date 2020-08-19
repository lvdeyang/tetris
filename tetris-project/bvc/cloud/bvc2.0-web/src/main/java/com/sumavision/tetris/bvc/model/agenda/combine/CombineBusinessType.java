package com.sumavision.tetris.bvc.model.agenda.combine;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum CombineBusinessType {

	AGENDA("议程"),
	GROUP("业务组");
	
	private String name;
	
	private CombineBusinessType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static CombineBusinessType fromName(String name) throws Exception{
		CombineBusinessType[] values = CombineBusinessType.values();
		for(CombineBusinessType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
