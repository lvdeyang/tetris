package com.sumavision.bvc.device.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 会议配置类型 
 * @author lvdeyang
 * @date 2018年8月4日 上午10:22:44 
 */
public enum ConfigType {

	AGENDA("会议议程"),
	SCHEME("会议方案"),
	VIRTUAL("虚拟配置"),
	DEFAULT("默认配置");
	
	private String name;
	
	private ConfigType(String name){
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
	public static ConfigType fromName(String name) throws Exception{
		ConfigType[] values = ConfigType.values();
		for(ConfigType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
