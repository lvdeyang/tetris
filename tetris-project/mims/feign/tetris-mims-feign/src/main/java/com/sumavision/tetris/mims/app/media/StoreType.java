package com.sumavision.tetris.mims.app.media;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum StoreType {
	LOCAL("本地存储"),
	REMOTE("远端存储");
	
	private String name;
	
	private StoreType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static StoreType fromName(String name) throws Exception{
		StoreType[] values = StoreType.values();
		for(StoreType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
