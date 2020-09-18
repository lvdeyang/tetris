package com.sumavision.bvc.device.monitor.record;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum MonitorRecordMode {

	MANUAL("手动"),
	SCHEDULING("定时"),
	TIMESEGMENT("排期"),
	CYCLE("循环");
	
	private String name;
	
	private MonitorRecordMode(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static MonitorRecordMode fromName(String name) throws Exception{
		MonitorRecordMode[] values = MonitorRecordMode.values();
		for(MonitorRecordMode value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
