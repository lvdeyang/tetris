package com.sumavision.bvc.device.monitor.record;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum MonitorRecordSourceType {

	//设备
	DEVICE("设备"),
	//合屏或混音
	COMBINE("联合");
	
	private String name;
	
	private MonitorRecordSourceType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static MonitorRecordSourceType fromName(String name) throws Exception{
		MonitorRecordSourceType[] values = MonitorRecordSourceType.values();
		for(MonitorRecordSourceType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
