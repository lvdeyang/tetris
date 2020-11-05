package com.sumavision.bvc.command.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 成员状态
 * @Description: 已连通、未连通、连通中
 * @author zsy
 * @date 2019年9月20日 下午1:22:00
 */
public enum MemberStatus {
	
	CONNECT("已连通"),
	DISCONNECT("未连通"),
	CONNECTING("连通中");//会议MEETING的发言业务没有此状态

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
