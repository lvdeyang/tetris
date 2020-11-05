package com.sumavision.tetris.easy.process.access.service;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 服务类型<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年12月13日 下午1:36:02
 */
public enum ServiceType {

	REST("rest服务");
	//WEBSERVICE("webservice");
	
	private String name;
	
	private ServiceType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static ServiceType fromName(String name) throws Exception{
		ServiceType[] values = ServiceType.values();
		for(ServiceType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
