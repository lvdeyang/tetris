package com.sumavision.bvc.device.monitor.osd;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * osd图层类型<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年5月6日 上午8:58:15
 */
public enum MonitorOsdLayerType {

	SUBTITLE("字幕"),
	DATE("日期", "OSD_DATE"),
	DATETIME("时间", "OSD_DATETIME"),
	DEVNAME("设备名称", "OSD_NAME");
	
	private String name;
	
	private String protocol;
	
	private MonitorOsdLayerType(String name){
		this.name = name;
	}
	
	private MonitorOsdLayerType(String name, String protocol){
		this.name = name;
		this.protocol = protocol;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getProtocol(){
		return this.protocol;
	}
	
	public static MonitorOsdLayerType fromName(String name) throws Exception{
		MonitorOsdLayerType[] values = MonitorOsdLayerType.values();
		for(MonitorOsdLayerType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
