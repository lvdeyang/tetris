package com.sumavision.tetris.user;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 用户分类<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年11月20日 上午11:38:11
 */
@Deprecated
public enum UserClassify {

	MAINTENANCE("运维"),
	COMPANY_ADMIN("企业管理员"),
	COMPANY_USER("企业用户"),
	NORMAL("普通用户");
	
	private String name;
	
	private UserClassify(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static UserClassify fromName(String name) throws Exception{
		UserClassify[] values = UserClassify.values();
		for(UserClassify value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}

