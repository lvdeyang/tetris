package com.sumavision.bvc.device.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 设备成员状态
 * @Description: stb用：连通、未连通
 * @author wjw
 * @date 2018年10月11日 下午3:33:41
 */
public enum MemberStatus {
	
	CONNECT("已连通"),
	DISCONNECT("未连通"),
	CONNECTING("连通中");

	private String name;
	
	private MemberStatus(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static MemberStatus fromName(String name) throws Exception{
		MemberStatus[] values = MemberStatus.values();
		for(MemberStatus value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
