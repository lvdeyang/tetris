package com.sumavision.tetris.bvc.model.agenda;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum LayoutPositionSelectionType {

	CONFIRM("确定的"),
	AUTO_INCREMENT("自适应");
	
	private String name;
	
	private LayoutPositionSelectionType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static LayoutPositionSelectionType fromName(String name) throws Exception{
		LayoutPositionSelectionType[] values = LayoutPositionSelectionType.values();
		for(LayoutPositionSelectionType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
