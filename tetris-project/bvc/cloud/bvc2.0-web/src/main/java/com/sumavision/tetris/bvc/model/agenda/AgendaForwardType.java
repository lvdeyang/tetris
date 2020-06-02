package com.sumavision.tetris.bvc.model.agenda;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum AgendaForwardType {

	VIDEO("视频"),
	AUDIO("音频");
	
	private String name;
	
	private AgendaForwardType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static AgendaForwardType fromName(String name) throws Exception{
		AgendaForwardType[] values = AgendaForwardType.values();
		for(AgendaForwardType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
