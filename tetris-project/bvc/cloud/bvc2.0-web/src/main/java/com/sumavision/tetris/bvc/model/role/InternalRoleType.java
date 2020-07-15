package com.sumavision.tetris.bvc.model.role;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum InternalRoleType {

	COMMAND_CHAIRMAN("指挥主席"),
	COMMAND_COOPERATION("协同指挥员"),
	COMMAND_SPECIFIC("专向指挥员"),
	COMMAND_AUDIENCE("指挥观众"),
	MEETIING_CHAIRMAN("会议主席"),
	MEETING_SPEAKER("会议发言人"),
	MEETING_AUDIENCE("会议观众"),
	VOD_DST("点播发起人"),
	VOD_SRC("被点播资源"),
	CALL_USER("呼叫双方");
	
	private String name;

	private InternalRoleType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static InternalRoleType fromName(String name) throws Exception{
		InternalRoleType[] values = InternalRoleType.values();
		for(InternalRoleType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
