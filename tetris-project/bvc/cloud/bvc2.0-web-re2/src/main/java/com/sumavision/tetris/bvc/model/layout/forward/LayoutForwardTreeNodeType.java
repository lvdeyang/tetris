package com.sumavision.tetris.bvc.model.layout.forward;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum LayoutForwardTreeNodeType {

	DECODE_CHANNEL("解码通道"),
	FORWARD("转发"),
	COMBINE_TEMPLATE("合屏模板"),
	COMBINE_TEMPLATE_POSITION("合屏模板布局"),
	LAYOUT_POSITION("虚拟源布局");
	
	private String name;
	
	private LayoutForwardTreeNodeType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static LayoutForwardTreeNodeType fromName(String name) throws Exception{
		LayoutForwardTreeNodeType[] values = LayoutForwardTreeNodeType.values();
		for(LayoutForwardTreeNodeType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
