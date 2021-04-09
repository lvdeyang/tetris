package com.sumavision.tetris.bvc.model.agenda;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum SourceType {

	ROLE_CHANNEL("角色通道"),
	GROUP_MEMBER_CHANNEL("会议成员通道"),
	COMBINE_VIDEO_VIRTUAL_SOURCE("合屏虚拟源");
	
	private String name;
	
	private SourceType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static SourceType fromName(String name) throws Exception{
		SourceType[] values = SourceType.values();
		for(SourceType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
