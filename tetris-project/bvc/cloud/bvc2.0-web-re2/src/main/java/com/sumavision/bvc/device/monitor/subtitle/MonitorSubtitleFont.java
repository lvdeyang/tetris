package com.sumavision.bvc.device.monitor.subtitle;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 字幕字体<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年5月6日 下午1:50:57
 */
public enum MonitorSubtitleFont {

	HEITI("黑体", "黑体"),
	SONTTI("宋体", "宋体"),
	KAITI("楷体", "楷体"),
	FANG_SONG("仿宋", "仿宋"),
	WEI_RUAN_YA_HEI("微软雅黑", "微软雅黑");
	
	private String name;
	
	private String protocol;
	
	private MonitorSubtitleFont(String name, String protocol){
		this.name = name;
		this.protocol = protocol;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getProtocol(){
		return this.protocol;
	}
	
	public static final MonitorSubtitleFont fromName(String name) throws Exception{
		MonitorSubtitleFont[] values = MonitorSubtitleFont.values();
		for(MonitorSubtitleFont value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
	public static final MonitorSubtitleFont fromProtocol(String protocol) throws Exception{
		MonitorSubtitleFont[] values = MonitorSubtitleFont.values();
		for(MonitorSubtitleFont value:values){
			if(value.getProtocol().equals(protocol)){
				return value;
			}
		}
		throw new ErrorTypeException("protocol", protocol);
	}
	
}
