package com.sumavision.tetris.bvc.business.terminal.hall;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum PrivilegeType {

	RECORD("录制", "-r"),
	VOD("点播", "-w"),
	CALL("呼叫", "-hj"),
	COMMAND("指挥", "-zk"),
	MEETING("会议", "-hy");
	
	private String name;
	
	private String code;
	
	private PrivilegeType(String name, String code){
		this.name = name;
		this.code = code;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getCode(){
		return this.code;
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
	
	public static PrivilegeType fromCode(String code) throws Exception{
		PrivilegeType[] values = PrivilegeType.values();
		for(PrivilegeType value:values){
			if(value.getCode().equals(code)){
				return value;
			}
		}
		throw new ErrorTypeException("code", code);
	}
	
}
