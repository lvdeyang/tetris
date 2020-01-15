package com.sumavision.bvc.device.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum GroupRegion {

	BEIJING("北京","0"),
	NEIMENG("内蒙","1"),
	SHANXI("山西","2");
	
	private String name;
	private String protocalId;
	
	private GroupRegion(String name, String protocalId) {
		this.name = name;
		this.protocalId = protocalId;
	}
	
	public String getName() {
		return this.name;
	}
	public String getProtocalId() {
		return this.protocalId;
	}
	
	public static GroupRegion fromName(String name) throws Exception {
		GroupRegion [] values = GroupRegion.values();
		for(GroupRegion value : values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
