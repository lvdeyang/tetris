package com.sumavision.tetris.bvc.model.agenda;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum LayoutSelectionType {

	CONFIRM("确定的"),
	ADAPTABLE("自适应");
	
	private String name;
	
	private LayoutSelectionType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static LayoutSelectionType fromName(String name) throws Exception{
		LayoutSelectionType[] values = LayoutSelectionType.values();
		for(LayoutSelectionType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
