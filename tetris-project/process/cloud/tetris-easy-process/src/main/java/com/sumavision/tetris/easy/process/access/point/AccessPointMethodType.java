package com.sumavision.tetris.easy.process.access.point;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 接入点方法类型<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月10日 上午10:02:31
 */
public enum AccessPointMethodType {

	HTTP_METHOD_POST("httpPost方法"),
	HTTP_METHOD_GET("httpGet方法");
	
	private String name;
	
	private AccessPointMethodType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static AccessPointMethodType fromName(String name) throws Exception{
		AccessPointMethodType[] values = AccessPointMethodType.values();
		for(AccessPointMethodType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
