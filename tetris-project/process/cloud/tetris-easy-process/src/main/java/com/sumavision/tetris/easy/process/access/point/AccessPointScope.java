package com.sumavision.tetris.easy.process.access.point;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 接入点作用域<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年12月26日 上午10:48:43
 */
public enum AccessPointScope {

	SYSTEMSCOPE("系统域"),
	USERSCOPE("用户域");
	
	private String name;
	
	private AccessPointScope(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static AccessPointScope fromName(String name) throws Exception{
		AccessPointScope[] values = AccessPointScope.values();
		for(AccessPointScope value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
