package com.sumavision.tetris.cs.channel;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum BroadWay {
	TERMINAL_BROAD("终端播发"),
	ABILITY_BROAD("轮播能力");
	
	private String name;

	public String getName() {
		return name;
	}
	
	private BroadWay(String name){
		this.name = name;
	}
	
	public static BroadWay fromName(String name) throws Exception{
		BroadWay[] values = BroadWay.values();
		for(BroadWay value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
