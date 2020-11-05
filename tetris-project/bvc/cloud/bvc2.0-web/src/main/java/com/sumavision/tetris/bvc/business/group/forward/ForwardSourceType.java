package com.sumavision.tetris.bvc.business.group.forward;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum ForwardSourceType {

	COMBINE_VIDEO("合屏"),
	CHANNEL_VIDEO("视频通道"),
	COMBINE_AUDIO("混音"),
	CHANNEL_AUDIO("音频通道");
	
	private String name;
	
	private ForwardSourceType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static ForwardSourceType fromName(String name) throws Exception{
		ForwardSourceType[] values = ForwardSourceType.values();
		for(ForwardSourceType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
