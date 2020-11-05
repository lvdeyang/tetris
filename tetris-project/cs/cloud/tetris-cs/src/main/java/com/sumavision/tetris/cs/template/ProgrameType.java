package com.sumavision.tetris.cs.template;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum ProgrameType {
	LABEL("标签"),
	MIMS("媒资");
	
	private String name;
	
	private ProgrameType(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public static ProgrameType fromName(String name) throws Exception{
		ProgrameType[] values = ProgrameType.values();
		for(ProgrameType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
