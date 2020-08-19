package com.sumavision.tetris.zoom.call.user;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum CallUserType {

	VIDEO_AUDIO("音视频呼叫"),
	AUDIO_ONLY("音频呼叫");
	
	private String name;
	
	private CallUserType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static CallUserType fromName(String name) throws Exception{
		CallUserType[] values = CallUserType.values();
		for(CallUserType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
