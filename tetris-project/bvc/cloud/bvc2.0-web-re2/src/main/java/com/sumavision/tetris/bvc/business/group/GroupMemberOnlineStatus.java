package com.sumavision.tetris.bvc.business.group;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum GroupMemberOnlineStatus {
	
	ONLINE("在线"),
	OFFLINE("不在线");

	private String name;
	
	private GroupMemberOnlineStatus(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static GroupMemberOnlineStatus fromName(String name) throws Exception{
		GroupMemberOnlineStatus[] values = GroupMemberOnlineStatus.values();
		for(GroupMemberOnlineStatus value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
