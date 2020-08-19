package com.sumavision.tetris.easy.process.access.point;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 标识参数的方向性<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年12月13日 下午4:12:47
 */
public enum ParamDirection {

	FORWARD("参数"),
	REVERSE("返回值");
	
	private String name;
	
	private ParamDirection(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static ParamDirection fromName(String name) throws Exception{
		ParamDirection[] values = ParamDirection.values();
		for(ParamDirection value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
