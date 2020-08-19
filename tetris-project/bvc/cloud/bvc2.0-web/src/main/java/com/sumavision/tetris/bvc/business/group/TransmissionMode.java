package com.sumavision.tetris.bvc.business.group;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum TransmissionMode {

	UNICAST("单播", "single"),
	MULTICAST("组播", "multicast");
	
	private String name;
	
	private String code;
	
	private TransmissionMode(String name, String code){
		this.name = name;
		this.code = code;
	}
	
	public String getName(){
		return this.name;
	}

	public String getCode() {
		return code;
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
