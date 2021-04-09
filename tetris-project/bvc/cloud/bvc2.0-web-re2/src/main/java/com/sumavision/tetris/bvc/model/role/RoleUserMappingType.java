package com.sumavision.tetris.bvc.model.role;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum RoleUserMappingType {

	ONE_TO_ONE("1:1", "只能授权一个用户"),
	ONE_TO_MANY("1:n", "可以授权多个用户");
	
	private String name;
	
	private String describe;
	
	private RoleUserMappingType(String name, String describe){
		this.name = name;
		this.describe = describe;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getDescribe(){
		return this.describe;
	}
	
	public static RoleUserMappingType fromName(String name) throws Exception{
		RoleUserMappingType[] values = RoleUserMappingType.values();
		for(RoleUserMappingType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
