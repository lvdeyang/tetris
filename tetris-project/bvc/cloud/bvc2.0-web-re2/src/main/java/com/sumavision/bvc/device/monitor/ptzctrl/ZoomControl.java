package com.sumavision.bvc.device.monitor.ptzctrl;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 镜头变倍控制<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年5月20日 下午3:49:47
 */
public enum ZoomControl {

	IN("放大", "in"),
	OUT("缩小", "out");
	
	private String name;
	
	private String protocol;
	
	private ZoomControl(String name, String protocol){
		this.name = name;
		this.protocol = protocol;
	}
	
	public String getName(){
		return this.name;
	}

	public String getProtocol(){
		return this.protocol;
	}
	
	public static ZoomControl fromName(String name) throws Exception{
		ZoomControl[] values = ZoomControl.values();
		for(ZoomControl value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
