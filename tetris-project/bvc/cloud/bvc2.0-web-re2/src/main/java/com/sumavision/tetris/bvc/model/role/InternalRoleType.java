package com.sumavision.tetris.bvc.model.role;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum InternalRoleType {

	COMMAND_CHAIRMAN("指挥主席",""),
	COMMAND_COOPERATION("协同指挥员",""),
	COMMAND_SPECIFIC("专向指挥员",""),
	COMMAND_AUDIENCE("指挥观众",""),
	MEETING_CHAIRMAN("会议主席",""),
	MEETING_SPEAKER("会议发言人",""),
	MEETING_AUDIENCE("会议观众",""),
	VOD_DST("点播发起人",""),
	VOD_SRC("被点播资源",""),
	CALLER_USER("主叫用户",""),
	CALLEE_USER("被叫用户",""),
	RECORD("录制角色",""),
	LEVEL_ZERO("零级指挥员","0"),
	LEVEL_ONE("一级指挥员","1"),
	LEVEL_TWO("二级指挥员","2"),
	LEVEL_THREE("三级指挥员","3"),
	LEVEL_FOUR("四级指挥员","4"),
	LEVEL_FIVE("五级指挥员","5"),
	LEVEL_SIX("六级指挥员","6");
	
	private String name;
	
	private String level;

	private InternalRoleType(String name, String level){
		this.name = name;
		this.level = level;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getLevel() {
		return level;
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
	
	public static InternalRoleType fromLevel(String level) throws Exception{
		InternalRoleType[] values = InternalRoleType.values();
		for(InternalRoleType value:values){
			if(value.getLevel().equals(level)){
				return value;
			}
		}
		throw new ErrorTypeException("level", level);
	}
	
}
