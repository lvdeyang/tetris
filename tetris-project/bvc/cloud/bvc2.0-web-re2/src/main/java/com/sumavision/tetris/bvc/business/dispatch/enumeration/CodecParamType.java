package com.sumavision.tetris.bvc.business.dispatch.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 参数模板类型 
 * @author zsy
 * @date 2020年3月17日 上午8:51:15
 */
public enum CodecParamType {
	
//	ID("id", "模板id"),
	AUTO("AUTO", "具体参数"),
	PARAM("PARAM", "具体参数");
//	DEFAULT("default", "预设值");
	
	private String name;
	
	private String description;
	
	private CodecParamType(String name, String description){
		this.name = name;
		this.description = description;
	}

	public String getName() {
		return name;
	}
	
	public String getDescription(){
		return description;
	}

	/**
	 * @Title: 根据名称获取组类型 
	 * @param name
	 * @throws Exception 
	 * @return CodecParamType 设备组类型 
	 */
	public static CodecParamType fromName(String name) throws Exception{
		CodecParamType[] values = CodecParamType.values();
		for(CodecParamType value: values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
