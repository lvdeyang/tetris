package com.sumavision.tetris.bvc.business.group;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum GroupMemberStatus {
	
	CONNECT("已连通"),
	DISCONNECT("未连通"),
	CONNECTING("连通中");

	private String name;
	
	private GroupMemberStatus(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static GroupMemberStatus fromName(String name) throws Exception{
		GroupMemberStatus[] values = GroupMemberStatus.values();
		for(GroupMemberStatus value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
