package com.sumavision.bvc.device.monitor.record;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum MonitorRecordStatus{

	WAITING("等待"),
	RUN("进行中"),
	STOP("已完成");
	
	private String name;
	
	private MonitorRecordStatus(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static MonitorRecordStatus fromName(String name) throws Exception{
		MonitorRecordStatus[] values = MonitorRecordStatus.values();
		for(MonitorRecordStatus value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
