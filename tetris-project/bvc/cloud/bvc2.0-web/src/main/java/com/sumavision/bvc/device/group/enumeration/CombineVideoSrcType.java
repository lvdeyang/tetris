package com.sumavision.bvc.device.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum CombineVideoSrcType {
	
	CHANNEL("通道"),
	VIRTUAL("虚拟");
	
	private String name;
	
	private CombineVideoSrcType(String name){
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
	public static CombineVideoSrcType fromName(String name) throws Exception{
		CombineVideoSrcType[] values = CombineVideoSrcType.values();
		for(CombineVideoSrcType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
