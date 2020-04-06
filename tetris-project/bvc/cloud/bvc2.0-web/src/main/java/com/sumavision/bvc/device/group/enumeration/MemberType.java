package com.sumavision.bvc.device.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 成员类型（暂时无用）
 * @Description: 设备/用户
 * @author zsy
 * @date 2018年10月9日
 */
public enum MemberType {
	
	DEVICE("设备", "ende"),
	ENCODER("编码设备", "en"),
	USER("用户", "usr");

	private String name;
	
	/** 标准协议中的成员类型 */
	private String protocalId;
	
	private MemberType(String name, String protocalId){
		this.name = name;
		this.protocalId = protocalId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getProtocalId() {
		return protocalId;
	}

	public void setProtocalId(String protocalId) {
		this.protocalId = protocalId;
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

	public static MemberType fromProtocalId(String protocalId) throws Exception{
		MemberType[] values = MemberType.values();
		for(MemberType value:values){
			if(value.getProtocalId().equals(protocalId)){
				return value;
			}
		}
		throw new ErrorTypeException("protocalId", protocalId);
	}
}
