package com.sumavision.tetris.bvc.model.agenda;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum AudioType {

	SILENCE("静音"),
	//INHERIT_VIDEO("与视频保持一致"),
	CUSTOM("自定义");
	
	private String name;
	
	private AudioType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static AudioType fromName(String name) throws Exception{
		AudioType[] values = AudioType.values();
		for(AudioType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
