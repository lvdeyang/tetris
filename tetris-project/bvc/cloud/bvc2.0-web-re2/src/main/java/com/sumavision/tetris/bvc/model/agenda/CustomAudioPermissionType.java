package com.sumavision.tetris.bvc.model.agenda;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum CustomAudioPermissionType {

	AGENDA("议程"),
	GROUP("会议"),
	AGENDA_FORWARD("议程转发");
	
	private String name;
	
	private CustomAudioPermissionType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static CustomAudioPermissionType fromName(String name) throws Exception{
		CustomAudioPermissionType[] values = CustomAudioPermissionType.values();
		for(CustomAudioPermissionType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
