package com.sumavision.tetris.bvc.business.dispatch.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 调度类型
 * @Description: 
 * @author zsy
 * @date 2019年9月20日 下午1:22:00
 */
public enum DispatchType {
	
	BUNDLE_BUNDLE("设备到设备"),
	BUNDLE_USER("设备到用户");
//	PAUSE("暂停"),
//	UNDONE("未执行");

	private String name;
	
	private DispatchType(String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public static DispatchType fromName(String name) throws Exception{
		DispatchType[] values = DispatchType.values();
		for(DispatchType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
