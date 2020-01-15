package com.sumavision.bvc.device.monitor.playback;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum MonitorRecordPlaybackTaskType {

	RECORD("录制文件调阅"),
	IMPORT("点播资源调阅");
	
	private String name;
	
	private MonitorRecordPlaybackTaskType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static MonitorRecordPlaybackTaskType fromName(String name) throws Exception{
		MonitorRecordPlaybackTaskType[] values = MonitorRecordPlaybackTaskType.values();
		for(MonitorRecordPlaybackTaskType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
