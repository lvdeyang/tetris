package com.sumavision.tetris.easy.process.access.point;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 参数打包格式<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年3月27日 下午1:40:25
 */
public enum ParamPackagingMethod {

	XML("xml格式"),
	FORMDATA("key-value格式"),
	JSON("json格式");
	
	private String name;
	
	private ParamPackagingMethod(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static final ParamPackagingMethod fromName(String name) throws Exception{
		ParamPackagingMethod[] values = ParamPackagingMethod.values();
		for(ParamPackagingMethod value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
