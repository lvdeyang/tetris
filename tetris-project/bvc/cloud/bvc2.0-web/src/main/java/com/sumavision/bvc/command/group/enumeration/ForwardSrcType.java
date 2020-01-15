package com.sumavision.bvc.command.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 转发源类型 
 * @author zsy
 * @date 2019年9月23日 上午10:11:22 
 */
public enum ForwardSrcType {
	
	ROLE("角色"),
//	USER("用户"),
//	DEVICE("设备"),
	NONE("无");
	
	private String name;
	
	private ForwardSrcType(String name){
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
	public static ForwardSrcType fromName(String name) throws Exception{
		ForwardSrcType[] values = ForwardSrcType.values();
		for(ForwardSrcType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
