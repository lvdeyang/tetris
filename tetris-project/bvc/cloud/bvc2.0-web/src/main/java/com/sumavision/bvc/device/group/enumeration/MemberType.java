package com.sumavision.bvc.device.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 成员类型
 * @Description: 设备/用户
 * @author zsy
 * @date 2018年10月9日
 */
public enum MemberType {
	
	DEVICE("设备"),
	USER("用户");

	private String name;
	
	private MemberType(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static MemberType fromName(String name) throws Exception{
		MemberType[] values = MemberType.values();
		for(MemberType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
