package com.sumavision.tetris.bvc.model.agenda.combine;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum CombineAudioSrcType {

	ROLE_CHANNEL("角色通道"),
	CHANNEL("设备通道");
	
	private String name;
	
	private CombineAudioSrcType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static CombineAudioSrcType fromName(String name) throws Exception{
		CombineAudioSrcType[] values = CombineAudioSrcType.values();
		for(CombineAudioSrcType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
