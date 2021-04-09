package com.sumavision.bvc.command.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 转发目的类型 
 * @author zsy
 * @date 2019年9月23日 上午10:11:22 
 */
public enum ForwardDstType {
	
	ROLE("角色"),
	USER("用户");
//	DEVICE("设备");
	
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
