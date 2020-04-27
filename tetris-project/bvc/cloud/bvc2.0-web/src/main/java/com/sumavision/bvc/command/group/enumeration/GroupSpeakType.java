package com.sumavision.bvc.command.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 发言类型 <br/>
 * @Description: 主席模式（即普通模式），讨论模式（全员互看）<br/>
 * @author zsy
 * @date 2020年3月26日 下午1:22:00
 */
public enum GroupSpeakType {

	CHAIRMAN("主席模式", "1", true),
	DISCUSS("讨论模式", "2", true);
	
	private String name;
	
	private String protocalId;
	
	private boolean show;
	
	private GroupSpeakType(String name, String protocalId, boolean show){
		this.name = name;
		this.protocalId = protocalId;
		this.show = show;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getProtocalId(){
		return this.protocalId;
	}
	
	public boolean getShow(){
		return this.show;
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
