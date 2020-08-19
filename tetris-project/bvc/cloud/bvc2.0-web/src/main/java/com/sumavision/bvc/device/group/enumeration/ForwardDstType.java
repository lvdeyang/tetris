package com.sumavision.bvc.device.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 转发目的类型 
 * @author lvdeyang
 * @date 2018年8月4日 上午11:36:04 
 */
public enum ForwardDstType {
	
	ROLE("角色"),
	CHANNEL("通道"),
	SCREEN("屏幕");
	
	private String name;
	
	private ForwardDstType(String name){
		this.name = name;
	}

	public String getName(){
		return this.name;
	}
	
	/**
	 * @Title: 根据名称获取转发目的类型 
	 * @param name 名称
	 * @throws Exception 
	 * @return ForwardDstType 转发目的类型
	 */
	public static ForwardDstType fromName(String name) throws Exception{
		ForwardDstType[] values = ForwardDstType.values();
		for(ForwardDstType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
