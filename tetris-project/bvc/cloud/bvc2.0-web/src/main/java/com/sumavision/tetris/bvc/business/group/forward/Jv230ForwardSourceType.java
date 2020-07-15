package com.sumavision.tetris.bvc.business.group.forward;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum Jv230ForwardSourceType {

	COMBINE_VIDEO("合屏"),
	CHANNEL_VIDEO("视频通道"),
	COMBINE_AUDIO("混音"),
	CHANNEL_AUDIO("音频通道");
	
	private String name;
	
	private Jv230ForwardSourceType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static Jv230ForwardSourceType fromName(String name) throws Exception{
		Jv230ForwardSourceType[] values = Jv230ForwardSourceType.values();
		for(Jv230ForwardSourceType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
