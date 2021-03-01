package com.sumavision.tetris.system.role;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum SystemRoleCreateType {

	SYSTEM_ADMIN("系统管理员"),
	COMPANY_ADMIN("公司管理员");
	
	private String name;
	
	private SystemRoleCreateType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static SystemRoleCreateType fromName(String name) throws Exception{
		SystemRoleCreateType[] values = SystemRoleCreateType.values();
		for(SystemRoleCreateType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
