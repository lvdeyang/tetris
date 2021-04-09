package com.sumavision.bvc.device.monitor.vod;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 协议类型<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年5月16日 上午8:43:12
 */
public enum ProtocolType {
	
	HTTP("http协议"),
	FTP("ftp协议");

	private String name;
	
	private ProtocolType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static ProtocolType fromName(String name) throws Exception{
		ProtocolType[] values = ProtocolType.values();
		for(ProtocolType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
