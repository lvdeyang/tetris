package com.sumavision.tetris.bvc.business.location;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum LocationExecuteStatus {
	
	RUN("运行中"),
	STOP("已停止");
	
	private String name;
	
	private LocationExecuteStatus(String name){
		this.name=name;
	}
	
	public String getName() {
		return name;
	}

	public static LocationExecuteStatus fromName(String name) throws Exception{
		LocationExecuteStatus[] values = LocationExecuteStatus.values();
		for(LocationExecuteStatus value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
