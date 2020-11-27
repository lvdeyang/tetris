package com.sumavision.bvc.device.monitor.record;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum MonitorRecordMode {

	MANUAL("长期","manual"),
	SCHEDULING("定时","datetime"),
	TIMESEGMENT("排期","time_segment");
	
	private String name;
	
	private String code;
	
	private MonitorRecordMode(String name,String code){
		this.name = name;
		this.code = code;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getCode(){
		return this.code;
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
	
	public static MonitorRecordMode fromCode(String code) throws Exception{
		MonitorRecordMode[] values = MonitorRecordMode.values();
		for(MonitorRecordMode value:values){
			if(value.getCode().equals(code)){
				return value;
			}
		}
		throw new ErrorTypeException("code", code);
	}
	
}
