package com.sumavision.bvc.device.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum RecordStatus {
	
	RUN("录制中"),
	STOP("未录制");
	
	private String name;
	
	private RecordStatus(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static RecordStatus fromName(String name) throws Exception{
		RecordStatus[] values = RecordStatus.values();
		for(RecordStatus value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
