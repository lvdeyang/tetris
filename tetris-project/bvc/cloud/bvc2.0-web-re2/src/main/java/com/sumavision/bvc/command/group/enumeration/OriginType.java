package com.sumavision.bvc.command.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 指挥（会议）、成员的来源类型 
 * @author zsy
 * @date 2020年3月31日 上午10:11:22 
 */
public enum OriginType {

	INNER("本系统"),
	OUTER("外部系统");
	
	private String name;
	
	private OriginType(String name){
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
	public static OriginType fromName(String name) throws Exception{
		OriginType[] values = OriginType.values();
		for(OriginType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
