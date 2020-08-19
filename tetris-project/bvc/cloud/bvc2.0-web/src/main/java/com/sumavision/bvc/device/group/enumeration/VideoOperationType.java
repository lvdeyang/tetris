package com.sumavision.bvc.device.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 视频操作类型 
 * @author lvdeyang
 * @date 2018年8月4日 上午10:50:18 
 */
public enum VideoOperationType {

	COMBINE("合屏"),
	FORWARD("转发");
	
	private String name;
	
	private VideoOperationType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	/**
	 * @Title: 根据名称获取视频操作方式
	 * @param name 名称
	 * @throws Exception 
	 * @return ConfigVideoType 视频操作方式
	 */
	public static VideoOperationType fromName(String name) throws Exception{
		VideoOperationType[] values = VideoOperationType.values();
		for(VideoOperationType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
