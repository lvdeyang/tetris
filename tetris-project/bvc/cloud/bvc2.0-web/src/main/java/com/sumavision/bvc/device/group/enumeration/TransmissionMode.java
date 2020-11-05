package com.sumavision.bvc.device.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum TransmissionMode {

	UNICAST("单播"),
	MULTICAST("组播");
	
	private String name;
	
	private TransmissionMode(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static TransmissionMode fromName(String name) throws Exception{
		TransmissionMode[] values = TransmissionMode.values();
		for(TransmissionMode value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
