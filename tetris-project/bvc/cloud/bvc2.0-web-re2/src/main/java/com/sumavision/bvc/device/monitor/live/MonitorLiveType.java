package com.sumavision.bvc.device.monitor.live;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 直播类型，主要区分是网页调阅还是设备调阅<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年4月26日 上午11:50:38
 */
public enum MonitorLiveType {

	USER_TWO_SIDE("用户双向通信"),
	WEBSITE_PLAYER("网页播放器任务"),
	DEVICE("设备任务");
	
	private String name;
	
	private MonitorLiveType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static MonitorLiveType fromName(String name) throws Exception{
		MonitorLiveType[] values = MonitorLiveType.values();
		for(MonitorLiveType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
