package com.sumavision.bvc.command.group.setup;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 可见范围
 * @author zsy
 * @date 2019年9月25日
 */
public enum VisibleRangeType {

	INSIDE_GROUP("组内"),
	ALL("全网");
	
	private String name;
	
	private VisibleRangeType(String name){
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
	public static VisibleRangeType fromName(String name) throws Exception{
		VisibleRangeType[] values = VisibleRangeType.values();
		for(VisibleRangeType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
