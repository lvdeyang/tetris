package com.sumavision.bvc.control.device.group.vo.tree.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum TreeNodeType {
	
	FOLDER("文件夹"),
	VOD_RESOURCE("点播资源"),
	ROLE("角色"),
	BUNDLE("设备"),
	SCREEN("屏幕"),
	CHANNEL("通道"),
	VIRTUAL("虚拟源"),
	USER("用户"),
	COMMAND("会议."),
	CONFERENCE_HALL("会场");
	
	private String name;
	
	private TreeNodeType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static TreeNodeType fromName(String name) throws Exception{
		TreeNodeType[] values = TreeNodeType.values();
		for(TreeNodeType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}

}
