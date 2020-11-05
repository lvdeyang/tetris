package com.sumavision.tetris.easy.process.access.point;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 参数类型<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年12月13日 下午3:32:06
 */
public enum ParamType {

	OBJECT("复杂类型"),
	ENUM("枚举"),
	ARRAY("数组"),
	BASIC("基本类型");
	
	private String name;
	
	private ParamType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static ParamType fromName(String name) throws Exception{
		ParamType[] values = ParamType.values();
		for(ParamType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
