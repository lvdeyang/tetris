package com.sumavision.tetris.auth.token;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 终端类型<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年12月6日 上午9:22:54
 */
public enum TerminalType {

	PC_PLATFORM("pc平台", "", 4*60*60),
	PC_PORTAL("pc门户", "", 4*60*60),
	ANDROID_PORTAL("安卓门户", "/api/terminal", 4*60*60),
	ANDROID_COLLECTING("安卓采集终端", "/api/android", 4*60*60),
	ANDROID_TVOS("安卓机顶盒", "/api/tvos", 4*60*60),
	QT_MEDIA_EDITOR("qt快编软件", "/api/qt", 4*60*60),
	QT_POLLING("qt轮询软件", "/api/polling", 4*60*60),
	QT_CATALOGUE("qt编单软件", "/api/schedule", 4*60*60),
	API("api调用", "/api/server", 0);
	
	/** 终端类型名称 */
	private String name;
	
	/** 拦截路径 */
	private String uriPrefix;
	
	/** token超时时间，单位：秒，取0永不超时 */
	private Integer tokenTimeout;
	
	private TerminalType(String name, String uriPrefix, Integer tokenTimeout){
		this.name = name;
		this.uriPrefix = uriPrefix;
		this.tokenTimeout = tokenTimeout;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getUriPrefix(){
		return this.uriPrefix;
	}
	
	public Integer getTokenTimeout(){
		return this.tokenTimeout;
	}
	
	public static TerminalType fromName(String name) throws Exception{
		TerminalType[] values = TerminalType.values();
		for(TerminalType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
