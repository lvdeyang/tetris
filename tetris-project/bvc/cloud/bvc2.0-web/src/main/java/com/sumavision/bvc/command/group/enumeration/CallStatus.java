package com.sumavision.bvc.command.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 通话状态
 * @author zsy
 * @date 2020年1月6日 下午1:22:00
 */
public enum CallStatus {

	WAITING_FOR_RESPONSE("等待回复", "waiting"),
	PAUSE("暂停", "pause"),
	ONGOING("通话中", "ongoing");
	
	private String name;
	
	private String code;
	
	private CallStatus(String name, String code){
		this.name = name;
		this.code = code;
	}
	
	public String getName(){
		return this.name;
	}
	
	public String getCode(){
		return this.code;
	}
	
	public static CallStatus fromName(String name) throws Exception{
		CallStatus[] values = CallStatus.values();
		for(CallStatus value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
