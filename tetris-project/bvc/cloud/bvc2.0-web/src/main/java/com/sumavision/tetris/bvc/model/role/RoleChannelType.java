package com.sumavision.tetris.bvc.model.role;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum RoleChannelType {

	VIDEO_ENCODE("视频编码"),
	AUDIO_ENCODE("音频编码"),
	VIDEO_DECODE("视频解码"),
	AUDIO_DECODE("音频解码");
	
	private String name;
	
	private RoleChannelType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static RoleChannelType fromName(String name) throws Exception{
		RoleChannelType[] values = RoleChannelType.values();
		for(RoleChannelType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
