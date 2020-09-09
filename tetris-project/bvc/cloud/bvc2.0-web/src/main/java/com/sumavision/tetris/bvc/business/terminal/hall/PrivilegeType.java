package com.sumavision.tetris.bvc.business.terminal.hall;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum PrivilegeType {

	RECORD("录制"),
	VOD("点播"),
	CALL("呼叫"),
	COMMAND("指挥");
	
	private String name;
	
	private PrivilegeType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static PrivilegeType fromName(String name) throws Exception{
		PrivilegeType[] values = PrivilegeType.values();
		for(PrivilegeType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
