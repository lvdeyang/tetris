package com.sumavision.bvc.basic.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 系统角色类型<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月12日 下午4:50:23
 */
public enum RoleSpecial {

	SPOKESMAN("发言人"),
	AUDIENCE("观众");
	
	private String name;
	
	private RoleSpecial(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public static RoleSpecial fromName(String name) throws Exception{
		RoleSpecial[] values = RoleSpecial.values();
		for(RoleSpecial value: values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
