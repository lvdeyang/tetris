package com.sumavision.bvc.system.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 会议模板内容类型 
 * @author lvdeyang
 * @date 2018年7月24日 下午4:05:23 
 */
public enum TplContentType {

	BUSINESSROLE("业务角色"),
	CHANNELNAME("通道别名"),
	RECORDSCHEME("录制方案"),
	SCREENLAYOUT("布局方案");
	
	private String name;
	
	private TplContentType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	/**
	 * @Title: 根据内容类型名称获取类型 <br/>
	 * @param name 内容类型名称
	 * @return TplContentType 内容类型
	 */
	public static TplContentType fromName(String name) throws Exception{
		TplContentType[] values = TplContentType.values();
		for(TplContentType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
