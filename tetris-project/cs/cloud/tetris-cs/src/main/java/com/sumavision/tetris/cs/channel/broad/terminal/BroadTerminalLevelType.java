package com.sumavision.tetris.cs.channel.broad.terminal;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum BroadTerminalLevelType {
	NORMAL("一般", 9),
	MORE("较大", 8),
	GREAT("重大", 7),
	VARY("很重大", 6),
	ESPECIALLY("特别重大", 5),
	PUSH_NORMAL("4级（一般）", 4),
	PUSH_MORE("3级（较大）", 3),
	PUSH_GREAT("2级（重大）", 2),
	PUSH_VARY("1级（特别重大）", 1),
	PUSH_ESPECIALLY("保留", 0);
	
	private String name;
	
	private Integer level;

	public String getName() {
		return name;
	}

	public Integer getLevel() {
		return level;
	}
	
	private BroadTerminalLevelType(String name, Integer level) {
		this.name = name;
		this.level = level;
	}
	
	public static BroadTerminalLevelType fromName(String name) throws Exception{
		BroadTerminalLevelType[] values = BroadTerminalLevelType.values();
		for(BroadTerminalLevelType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("优先级", name);
	}
	
	public static BroadTerminalLevelType fromLevel(Integer level) throws Exception{
		BroadTerminalLevelType[] values = BroadTerminalLevelType.values();
		for(BroadTerminalLevelType value:values){
			if(value.getLevel().equals(level)){
				return value;
			}
		}
		throw new ErrorTypeException("优先级", level);
	}
}
