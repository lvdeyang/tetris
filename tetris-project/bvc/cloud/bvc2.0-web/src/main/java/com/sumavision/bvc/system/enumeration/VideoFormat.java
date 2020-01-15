package com.sumavision.bvc.system.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 视频编码格式 
 * @author lvdeyang
 * @date 2018年7月25日 下午2:48:24 
 */
public enum VideoFormat {

	H_265("h265"),
	H_264("h264");
	
	private String name;
	
	private VideoFormat(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	/**
	 * @Title: 根据名称获取视频编码格式 
	 * @param name 
	 * @return VideoFormat 视频编码格式
	 */
	public static VideoFormat fromName(String name) throws Exception{
		VideoFormat[] values = VideoFormat.values();
		for(VideoFormat value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
