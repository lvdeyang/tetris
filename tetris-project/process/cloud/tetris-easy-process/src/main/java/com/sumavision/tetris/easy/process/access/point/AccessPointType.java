package com.sumavision.tetris.easy.process.access.point;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 流程接入点类型<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年12月13日 下午1:12:54
 */
public enum AccessPointType {

	INTERNAL("内置接口"),
	REMOTE_SYNCHRONOUS("远程同步接口"),
	REMOTE_ASYNCHRONOUS("远程异步接口");
	
	private String name;
	
	private AccessPointType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static final AccessPointType fromName(String name) throws Exception{
		AccessPointType[] values = AccessPointType.values();
		for(AccessPointType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
