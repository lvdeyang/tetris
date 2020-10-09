package com.sumavision.tetris.mims.app.boss;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum MediaType {

	VEDIO("视频"),
	AUDIO("音频");
	
	private String name;
	
	private MediaType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static MediaType fromName(String name) throws Exception{
		MediaType[] values = MediaType.values();
		for(MediaType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
