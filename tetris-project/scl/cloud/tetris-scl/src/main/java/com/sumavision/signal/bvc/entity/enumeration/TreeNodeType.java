package com.sumavision.signal.bvc.entity.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum TreeNodeType {
	
	FOLDER("文件夹"),
	REPEATER("转发器"),
	INTERNET_ACCESS("网口");
	
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
