package com.sumavision.bvc.device.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 用于描述转发时源的类型
 * @author zy
 * @date 2018年7月31日 下午2:14:57 
 */
public enum ForwardSourceType {
	
	COMBINEVIDEO("合屏转发"),
	FORWARVIDEO("视频转发"),
	COMBINEAUDIO("混音转发"),
	FORWARDAUDIO("音频转发");
	
	private String name;
	
	private ForwardSourceType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	/**
	 * @Title: 根据名称获取转发时源的类型<br/>
	 * @param name
	 * @return 转发时源的类型
	 */
	public static ForwardSourceType fromName(String name) throws Exception{
		ForwardSourceType[] values = ForwardSourceType.values();
		for(ForwardSourceType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}

}
