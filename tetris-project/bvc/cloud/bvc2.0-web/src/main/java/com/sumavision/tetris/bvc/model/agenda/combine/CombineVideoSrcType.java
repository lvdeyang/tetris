package com.sumavision.tetris.bvc.model.agenda.combine;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum CombineVideoSrcType {
	
	ROLE_CHANNEL("角色通道"),
	CHANNEL("设备通道"),
	VIRTUAL("虚拟屏幕");
	
	private String name;
	
	private CombineVideoSrcType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static CombineVideoSrcType fromName(String name) throws Exception{
		CombineVideoSrcType[] values = CombineVideoSrcType.values();
		for(CombineVideoSrcType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}

}
