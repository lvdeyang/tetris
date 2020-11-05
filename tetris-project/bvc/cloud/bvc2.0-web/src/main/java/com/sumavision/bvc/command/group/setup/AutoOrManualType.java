package com.sumavision.bvc.command.group.setup;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 自动/手动类型 
 * @author zsy
 * @date 2019年9月25日
 */
public enum AutoOrManualType {

	AUTO("自动"),
	MANUAL("手动");
	
	private String name;
	
	private AutoOrManualType(String name){
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
	public static AutoOrManualType fromName(String name) throws Exception{
		AutoOrManualType[] values = AutoOrManualType.values();
		for(AutoOrManualType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
