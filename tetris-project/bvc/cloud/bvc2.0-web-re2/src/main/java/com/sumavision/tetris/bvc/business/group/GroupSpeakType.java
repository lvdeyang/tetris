package com.sumavision.tetris.bvc.business.group;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 发言类型 <br/>
 * @Description: 主席模式（即普通模式），讨论模式（全员互看）<br/>
 * @author zsy
 * @date 2020年3月26日 下午1:22:00
 */
public enum GroupSpeakType {

	CHAIRMAN("主席模式", "0"),
	DISCUSS("讨论模式", "1");
	
	private String name;
	
	private String protocalId;//按标准，0表示主席模式、1表示讨论模式
	
	private GroupSpeakType(String name, String protocalId){
		this.name = name;
		this.protocalId = protocalId;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getProtocalId(){
		return this.protocalId;
	}
	
	/**
	 * @Title: 根据名称获取组类型 
	 * @param name
	 * @throws Exception 
	 * @return GroupType 设备组类型 
	 */
	public static GroupSpeakType fromName(String name) throws Exception{
		GroupSpeakType[] values = GroupSpeakType.values();
		for(GroupSpeakType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
