package com.sumavision.tetris.mims.app.role;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 媒资角色分类<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月25日 下午3:52:55
 */
public enum RoleClassify {

	INTERNAL_COMPANY_ADMIN_ROLE("内置公司管理员角色");
	
	private String name;
	
	private RoleClassify(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static RoleClassify fromName(String name) throws Exception{
		RoleClassify[] values = RoleClassify.values();
		for(RoleClassify value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
