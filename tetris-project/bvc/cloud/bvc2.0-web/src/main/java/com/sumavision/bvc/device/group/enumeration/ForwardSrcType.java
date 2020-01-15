package com.sumavision.bvc.device.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 转发源类型<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年10月14日 下午4:49:46
 */
public enum ForwardSrcType {

	ROLE("角色"),
	CHANNEL("通道"),
	VIRTUAL("虚拟");
	
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
