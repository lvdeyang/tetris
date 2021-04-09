package com.sumavision.tetris.bvc.page;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum SourceType {

	COMBINE_VIDEO("合屏", "combineVideo"),
	COMBINE_AUDIO("混音", "combineAudio"),
	CHANNEL("通道", "channel");
	
	private String name;
	
	private String protocalName;
	
	private SourceType(String name, String protocalName){
		this.name = name;
		this.protocalName = protocalName;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getProtocalName(){
		return protocalName;
	}
	
	public static SourceType fromName(String name) throws Exception{
		SourceType[] values = SourceType.values();
		for(SourceType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
