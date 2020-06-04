package com.sumavision.tetris.bvc.business.group;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum GroupStatus {

	START("开始"),
	STOP("停止"),
	PAUSE("暂停"),
	REMIND("提醒中");
	
	private String name;
	
	private GroupStatus(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static GroupStatus fromName(String name) throws Exception{
		GroupStatus[] values = GroupStatus.values();
		for(GroupStatus value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
