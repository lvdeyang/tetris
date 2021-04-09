package com.sumavision.bvc.command.group.setup;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 字幕设置
 * @author zsy
 * @date 2019年9月25日
 */
public enum OsdSetupType {

	AVAILABLE("启用"),
	FORBIDDEN("禁用");
	
	private String name;
	
	private OsdSetupType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	/**
	 * @Title: 根据名称获取会议配置类型 
	 * @param name 名称
	 * @throws Exception 
	 * @return ConfigType 会议配置类型
	 */
	public static OsdSetupType fromName(String name) throws Exception{
		OsdSetupType[] values = OsdSetupType.values();
		for(OsdSetupType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
