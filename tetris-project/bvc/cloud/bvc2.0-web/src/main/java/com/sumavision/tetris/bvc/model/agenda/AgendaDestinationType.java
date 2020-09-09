package com.sumavision.tetris.bvc.model.agenda;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum AgendaDestinationType {
	
	ROLE("业务角色"),
	ROLE_CHANNEL("业务角色"),
	//ROLE_COLLECTION("业务角色通道集合"),
	BUNDLE("设备"),
	CHANNEL("设备通道");
	
	private String name;
	
	private AgendaDestinationType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static AgendaDestinationType fromName(String name) throws Exception{
		AgendaDestinationType[] values = AgendaDestinationType.values();
		for(AgendaDestinationType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
