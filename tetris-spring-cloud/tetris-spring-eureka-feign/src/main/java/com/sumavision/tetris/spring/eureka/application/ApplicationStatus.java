package com.sumavision.tetris.spring.eureka.application;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum ApplicationStatus {

	UP("在线", "UP"),
	DOWN("离线", "DOWN");
	
	private String name;
	
	private String code;
	
	private ApplicationStatus(String name, String code){
		this.name = name;
		this.code = code;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getCode(){
		return this.code;
	}
	
	public static ApplicationStatus fromName(String name) throws Exception{
		ApplicationStatus[] values = ApplicationStatus.values();
		for(ApplicationStatus value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
	public static ApplicationStatus fromCode(String code) throws Exception{
		ApplicationStatus[] values = ApplicationStatus.values();
		for(ApplicationStatus value:values){
			if(value.getCode().equals(code)){
				return value;
			}
		}
		throw new ErrorTypeException("code", code);
	}
	
}
