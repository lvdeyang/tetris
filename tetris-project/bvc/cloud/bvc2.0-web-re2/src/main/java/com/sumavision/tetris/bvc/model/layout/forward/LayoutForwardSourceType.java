package com.sumavision.tetris.bvc.model.layout.forward;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum LayoutForwardSourceType {

	COMBINE_TEMPLATE("合屏模板"),
	LAYOUT_POSITION("虚拟源分屏");
	
	private String name;
	
	private LayoutForwardSourceType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static LayoutForwardSourceType fromName(String name) throws Exception{
		LayoutForwardSourceType[] values = LayoutForwardSourceType.values();
		for(LayoutForwardSourceType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
