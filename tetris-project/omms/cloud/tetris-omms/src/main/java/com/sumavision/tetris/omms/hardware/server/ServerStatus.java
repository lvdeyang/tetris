package com.sumavision.tetris.omms.hardware.server;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum ServerStatus {

	ONLINE("在线"),
	OFFLINE("离线");
	
	private String name;
	
	public String getName(){
		return this.name;
	}
	
	private ServerStatus(String name){
		this.name = name;
	}
	
	public static ServerStatus fromName(String name) throws Exception{
		ServerStatus[] values = ServerStatus.values();
		for(ServerStatus value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
