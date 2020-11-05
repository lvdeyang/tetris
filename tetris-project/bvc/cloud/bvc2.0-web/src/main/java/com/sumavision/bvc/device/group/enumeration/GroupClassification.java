package com.sumavision.bvc.device.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum GroupClassification {
	
	PROVINCE("省", "0"),
	CITY("市", "1"),
	AREA("区", "2");
	
	private String name;
	private String protocalId;
	
	private GroupClassification(String name, String protocalId){
		this.name = name;
		this.protocalId = protocalId;
	}
	
	public String getName() {
		return this.name;
	}
	public String getProtocalId() {
		return this.protocalId;
	}
	
	public static GroupClassification fromName(String name) throws Exception {
		GroupClassification [] values = GroupClassification.values();
		for(GroupClassification value : values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
