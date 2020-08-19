package com.sumavision.bvc.device.monitor.ptzctrl;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 光圈操作<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年5月20日 下午3:49:19
 */
public enum ApertureControl {

	PLUS("放大", "+"),
	MINUS("缩小", "-");
	
	private String name;
	
	private String protocol;
	
	private ApertureControl(String name, String protocol){
		this.name = name;
		this.protocol = protocol;
	}
	
	public String getName(){
		return this.name;
	}

	public String getProtocol(){
		return this.protocol;
	}
	
	public static ApertureControl fromName(String name) throws Exception{
		ApertureControl[] values = ApertureControl.values();
		for(ApertureControl value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
