package com.sumavision.tetris.zoom;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum ZoomSecretLevel {

	PUBLIC("公开的会议"),
	PRIVATE("私密的会议");
	
	private String name;
	
	private ZoomSecretLevel(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static ZoomSecretLevel fromName(String name) throws Exception{
		ZoomSecretLevel[] values = ZoomSecretLevel.values();
		for(ZoomSecretLevel value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
