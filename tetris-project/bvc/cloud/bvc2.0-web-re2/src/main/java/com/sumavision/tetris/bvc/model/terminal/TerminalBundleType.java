package com.sumavision.tetris.bvc.model.terminal;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum TerminalBundleType {

	ENCODER("编码设备"),
	DECODER("解码设备"),
	ENCODER_DECODER("编解码设备");
	
	private String name;
	
	private TerminalBundleType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static TerminalBundleType fromName(String name) throws Exception{
		TerminalBundleType[] values = TerminalBundleType.values();
		for(TerminalBundleType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
