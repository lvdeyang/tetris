package com.sumavision.tetris.bvc.model.terminal;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum TerminalType {

	JV210("jv210"),
	JV220("jv220"),
	ANDROID_TVOS("机顶盒"),
	QT_ZK("qt指控终端"),
	PC_PLATFORM("web页面");
	
	private String name;
	
	private TerminalType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static TerminalType fromName(String name) throws Exception{
		TerminalType[] values = TerminalType.values();
		for(TerminalType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
