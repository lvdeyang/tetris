package com.sumavision.tetris.bvc.model.agenda;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum AgendaSourceType {

	ROLE("业务角色"),
	ROLE_CHANNEL("业务角色通道"),
	//ROLE_COLLECTION("业务角色通道集合"),
	COMBINE_VIDEO("合屏"),
	COMBINE_AUDIO("混音"),
	BUNDLE("设备"),
	CHANNEL("设备通道");
	
	private String name;
	
	private AgendaSourceType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static AgendaSourceType fromName(String name) throws Exception{
		AgendaSourceType[] values = AgendaSourceType.values();
		for(AgendaSourceType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
