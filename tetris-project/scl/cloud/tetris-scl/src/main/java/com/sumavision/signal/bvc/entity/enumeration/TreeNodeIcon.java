package com.sumavision.signal.bvc.entity.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum TreeNodeIcon {

	FOLDER("icon-folder-close"),
	REPEATER("icon-tablet"),
	INTERNET_ACCESS("icon-rss");

	private String name;
	
	private TreeNodeIcon(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static TreeNodeIcon fromName(String name) throws Exception{
		TreeNodeIcon[] values = TreeNodeIcon.values();
		for(TreeNodeIcon value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
