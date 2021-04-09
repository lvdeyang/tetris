package com.sumavision.tetris.bvc.model.agenda;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum AudioPriority {

	GLOBAL_FIRST("全局优先"),
	AGENDA_FIRST("议程优先");
	
	private String name;
	
	private AudioPriority(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static AudioPriority fromName(String name) throws Exception{
		AudioPriority[] values = AudioPriority.values();
		for(AudioPriority value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
