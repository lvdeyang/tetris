package com.sumavision.tetris.omms.graph;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum ServerType {

	EUREKA("TETRIS-SPRING-EUREKA", "注册中心", "", GroupType.HEART),
	ZUUL("TETRIS-SPRING-ZUUL", "网关服务", "", GroupType.TETRIS),
	USER("TETRIS-USER", "用户服务", "", GroupType.TETRIS),
	MENU("TETRIS-MENU", "菜单服务", "", GroupType.TETRIS),
	MIMS("TETRIS-MIMS", "媒资服务", "", GroupType.TETRIS),
	PROCESS("TETRIS-EASY-PROCESS", "流程引擎", "", GroupType.TETRIS),
	CS("TETRIS-CS", "轮播服务", "", GroupType.TETRIS),
	CMS("TETRIS-CMS", "内容管理", "", GroupType.TETRIS),
	P2P("TETRIS-P2P", "点对点", "", GroupType.TETRIS),
	CAPACITY("TETRIS-CAPACITY", "能力接入", "", GroupType.TETRIS),
	OMMS("TETRIS-OMMS", "运维管理", "", GroupType.TETRIS),
	STREAM_MEDIA("PROTOCOL-CONVERSION-STREAM-MEDIA", "流媒体", "", GroupType.PROTOCOL_CONVERSION),
	FILE_TRANSCODING("PROTOCOL-CONVERSION-FILE-TRANSCODING", "文件转码", "", GroupType.PROTOCOL_CONVERSION),
	STREAM_TRANSCODING("PROTOCOL-CONVERSION-STREAM-TRANSCODING", "流转码", "", GroupType.PROTOCOL_CONVERSION),
	RECORD("PROTOCOL-CONVERSION-RECORD", "收录", "", GroupType.PROTOCOL_CONVERSION),
	PACKAGING("PROTOCOL-CONVERSION-PACKAGING", "软封装", "", GroupType.PROTOCOL_CONVERSION);
	
	private String id;
	
	private String name;
	
	private String icon;
	
	private GroupType groupType;
	
	private ServerType(String id, String name, String icon, GroupType groupType){
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.groupType = groupType;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getIcon() {
		return icon;
	}
	
	public GroupType getGroupType(){
		return groupType;
	}
	
	public static ServerType fromId(String id) throws Exception{
		ServerType[] values = ServerType.values();
		for(ServerType value:values){
			if(value.getId().equals(id)){
				return value;
			}
		}
		throw new ErrorTypeException("id", id);
	}
	
	public static ServerType fromName(String name) throws Exception{
		ServerType[] values = ServerType.values();
		for(ServerType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
