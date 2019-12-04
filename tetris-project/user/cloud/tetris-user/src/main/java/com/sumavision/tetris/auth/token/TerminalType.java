package com.sumavision.tetris.auth.token;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum TerminalType {

	PC_PLATFORM("pc平台"),
	PC_PORTAL("pc门户"),
	ANDROID_PORTAL("安卓门户"),
	ANDROID_COLLECTING("安卓采集终端"),
	QT_MEDIA_EDITOR("qt快编软件"),
	QT_CATALOGUE("qt编单软件"),
	API("api调用");
	
	private String name;
	
	private TerminalType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static TerminalType fromName(String name) throws Exception{
		TerminalType[] values = TerminalType.values();
		for(TerminalType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
