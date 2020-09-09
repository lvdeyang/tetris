package com.sumavision.tetris.menu;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum SystemRoleMenuPermissionFlag {

	HOME_PAGE("首页");
	
	private String name;
	
	private SystemRoleMenuPermissionFlag(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static SystemRoleMenuPermissionFlag fromName(String name) throws Exception{
		SystemRoleMenuPermissionFlag[] values = SystemRoleMenuPermissionFlag.values();
		for(SystemRoleMenuPermissionFlag value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
