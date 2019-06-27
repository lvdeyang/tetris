package com.sumavision.tetris.subordinate.role;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum SubordinateRoleClassify {
	INTERNAL_COMPANY_ADMIN_ROLE("内置公司管理员角色"),
	INTERNAL_COMPANY_ORDINARY_ROLE("内置公司普通角色");
	private String name;
	
	private SubordinateRoleClassify(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static SubordinateRoleClassify fromName(String name) throws Exception{
		SubordinateRoleClassify[] values = SubordinateRoleClassify.values();
		for(SubordinateRoleClassify value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
