package com.sumavision.tetris.bvc.business.group;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum GroupMemberType {

	MEMBER_USER("用户成员"),
	MEMBER_HALL("虚拟设备"),//会场
	MEMBER_DEVICE("设备成员"),
	RESOURCE_USER("用户资源"),
	RESOURCE_ENCODER("编码资源"),
	RESOURCE_FILE("文件资源");
	
	private String name;
	
	private GroupMemberType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static GroupMemberType fromName(String name) throws Exception{
		GroupMemberType[] values = GroupMemberType.values();
		for(GroupMemberType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
