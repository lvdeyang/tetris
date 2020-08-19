package com.sumavision.tetris.zoom.call.user;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum CallUserStatus {
	
	INVITE("邀请中"),
	IN_THE_CALL("通话中");
	
	private String name;
	
	private CallUserStatus(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static CallUserStatus fromName(String name) throws Exception{
		CallUserStatus[] values = CallUserStatus.values();
		for(CallUserStatus value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}

}
