package com.sumavision.bvc.device.monitor.live;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 点播任务目标设备类型<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年6月18日 下午3:20:36
 */
public enum DstDeviceType {

	WEBSITE_PLAYER("网页播放器"),
	DEVICE("设备");
	
	private String name;
	
	private DstDeviceType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static DstDeviceType fromName(String name) throws Exception{
		DstDeviceType[] values = DstDeviceType.values();
		for(DstDeviceType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
