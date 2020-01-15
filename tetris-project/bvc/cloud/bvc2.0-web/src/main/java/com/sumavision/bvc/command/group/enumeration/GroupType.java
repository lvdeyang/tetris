package com.sumavision.bvc.command.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 指挥组类型 <br/>
 * @Description: 普通，专向；会议<br/>
 * @author zsy
 * @date 2019年9月20日 下午1:22:00
 */
public enum GroupType {

	BASIC("普通", "1", true),
	COOPERATE("协同", "2", true),
	SECRET("专向", "3", true),
	MEETING("会议", "11", true);
	
	private String name;
	
	private String protocalId;
	
	private boolean show;
	
	private GroupType(String name, String protocalId, boolean show){
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
	public static GroupType fromName(String name) throws Exception{
		GroupType[] values = GroupType.values();
		for(GroupType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
