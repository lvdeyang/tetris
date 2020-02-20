package com.sumavision.tetris.cs.channel.broad.terminal;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum BroadTerminalLevelType {
	NORMAL("一般", 9),
	MORE("较大", 8),
	GREAT("重大", 7),
	VARY("很重大", 6),
	ESPECIALLY("特别重大", 5);
	
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
}
