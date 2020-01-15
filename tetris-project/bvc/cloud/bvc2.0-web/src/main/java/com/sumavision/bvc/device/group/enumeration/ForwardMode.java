package com.sumavision.bvc.device.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum ForwardMode {

	DEVICE("设备转发模式"),
	ROLE("角色转发模式");
	
	private String name;
	
	private ForwardMode(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public static ForwardMode fromName(String name) throws Exception{
		ForwardMode[] values = ForwardMode.values();
		for(ForwardMode value: values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		
		throw new ErrorTypeException("name", name);
	}
	
}
