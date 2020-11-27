package com.sumavision.tetris.cs.channel.broad.ability;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum OutputType {
	UDP_TS("UDP_TS"),
	RTMP("RTMP");
	
	private String name;

	public String getName() {
		return name;
	}
	
	private OutputType(String name) {
		this.name = name;
	}
	public static OutputType fromName(String name) throws Exception{
		OutputType[] values = OutputType.values();
		for (OutputType codingType : values) {
			if(codingType.getName().equals(name)){
				return codingType;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}