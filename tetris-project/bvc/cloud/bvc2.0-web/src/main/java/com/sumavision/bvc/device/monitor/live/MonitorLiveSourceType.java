package com.sumavision.bvc.device.monitor.live;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 直播源类型<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年4月16日 下午7:35:28
 */
public enum MonitorLiveSourceType {

	DEVICE("设备"),
	COMBINE("合屏");
	
	private String name;
	
	private MonitorLiveSourceType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static MonitorLiveSourceType fromName(String name) throws Exception{
		MonitorLiveSourceType[] values = MonitorLiveSourceType.values();
		for(MonitorLiveSourceType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
