package com.sumavision.bvc.device.monitor.record;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 录制类型标注是录制用户还是录制设备<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年6月4日 下午1:16:14
 */
public enum MonitorRecordType {

	//为了兼容老数据
	@Deprecated
	DEVICE("设备"),
	@Deprecated
	USER("用户"),
	
	LOCAL_DEVICE("录制本地设备"),
	XT_DEVICE("录制跨系统设备"),
	LOCAL_USER("录制本地用户"),
	XT_USER("录制跨系统用户");
	
	private String name;
	
	private MonitorRecordType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static MonitorRecordType fromName(String name) throws Exception{
		MonitorRecordType[] values = MonitorRecordType.values();
		for(MonitorRecordType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
