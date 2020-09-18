package com.sumavision.bvc.device.monitor.record;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum MonitorRecordManyTimesMode {
	DAY("day"),
	WEEK("week"),
	MONTH("month");
	
	String name;
	
	private MonitorRecordManyTimesMode(String name){
		this.name=name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public MonitorRecordManyTimesMode forName(String name) throws Exception{
		MonitorRecordManyTimesMode [] values=MonitorRecordManyTimesMode.values();
		for(MonitorRecordManyTimesMode value:values){
			if(value.equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
