package com.sumavision.bvc.device.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 设备组状态 
 * @author lvdeyang
 * @date 2018年8月1日 下午3:32:33 
 */
public enum GroupStatus {

	START("已开始"),
	STOP("已停止");
	
	private String name;
	
	private GroupStatus(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static GroupStatus fromName(String name) throws Exception{
		GroupStatus[] values = GroupStatus.values();
		for(GroupStatus value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
