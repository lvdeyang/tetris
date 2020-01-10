package com.sumavision.tetris.omms.graph;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum GroupType {

	HEART("heart", "微服务核心"),
	TETRIS("tetris", "微服务实例"),
	PROTOCOL_CONVERSION("protocol-conversion", "协议转换");
	
	private String id;
	
	private String name;
	
	private GroupType(String id, String name){
		this.id = id;
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}
	
	public static GroupType fromId(String id) throws Exception{
		GroupType[] values = GroupType.values();
		for(GroupType value:values){
			if(value.getId().equals(id)){
				return value;
			}
		}
		throw new ErrorTypeException("id", id);
	}
	
	public static GroupType fromName(String name) throws Exception{
		GroupType[] values = GroupType.values();
		for(GroupType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
