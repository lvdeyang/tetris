package com.sumavision.tetris.zoom.history;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum HistoryType {

	ZOOM_CODE("会议号码");
	
	private String name;
	
	private HistoryType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static HistoryType fromName(String name) throws Exception{
		HistoryType[] values = HistoryType.values();
		for(HistoryType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
