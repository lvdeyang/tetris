package com.sumavision.tetris.cs.channel;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum ChannelType {
	REMOTE("远程频道"),
	LOCAL("本地频道"),
	YJGB("应急广播下发排期");
	
	private String name;
	
	private ChannelType(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public static ChannelType fromName(String name) throws Exception{
		ChannelType[] values = ChannelType.values();
		for(ChannelType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
