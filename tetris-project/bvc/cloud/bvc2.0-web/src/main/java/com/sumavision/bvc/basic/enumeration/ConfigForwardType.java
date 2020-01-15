package com.sumavision.bvc.basic.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 系统议程转发类型<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月12日 下午4:06:44
 */
public enum ConfigForwardType {

	FORWARD("转发"),
	CONBINEVIDEO("合屏");
	
	private String name;
	
	private ConfigForwardType(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public static ConfigForwardType fromName(String name) throws Exception{
		ConfigForwardType[] values = ConfigForwardType.values();
		for(ConfigForwardType value: values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
