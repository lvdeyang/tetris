package com.sumavision.tetris.bvc.business.group;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum GroupLock {
	
	COMMON_LOCK("锁定会议");

	private String name;
	
	private GroupLock(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static GroupLock fromName(String name) throws Exception{
		GroupLock[] values = GroupLock.values();
		for(GroupLock value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
