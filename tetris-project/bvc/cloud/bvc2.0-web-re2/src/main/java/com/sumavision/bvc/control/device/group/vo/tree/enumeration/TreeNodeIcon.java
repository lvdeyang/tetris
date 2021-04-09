package com.sumavision.bvc.control.device.group.vo.tree.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum TreeNodeIcon {
	
	FOLDER("icon-folder-close"),
	VOD_RESOURCE("icon-file-alt"),
	BUNDLE("icon-facetime-video"),
	CHANNEL("icon-exchange"),
	CHAIRMAN("icon-user-md"),
	SPOKESMAN("icon-user"),
	VIRTUAL("icon-cloud"),
	GROUP("icon-group");

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
