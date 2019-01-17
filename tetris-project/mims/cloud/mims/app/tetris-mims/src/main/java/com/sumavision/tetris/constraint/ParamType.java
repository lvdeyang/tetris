package com.sumavision.tetris.constraint;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 参数类型<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年12月20日 下午4:05:00
 */
public enum ParamType {

	TARGET("带测值"),
	BOOLEAN("布尔值"),
	NUMBER("数值"),
	STRING("字符串"),
	ENUM("枚举");
	
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
