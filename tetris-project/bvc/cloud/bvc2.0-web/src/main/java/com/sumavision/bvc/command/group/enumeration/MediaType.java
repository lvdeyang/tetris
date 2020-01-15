package com.sumavision.bvc.command.group.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 媒体类型 
 * @author zsy
 * @date 2019年9月23日 上午10:11:22 
 */
public enum MediaType {

	VIDEO("视频"),
	AUDIO("音频");
	
	private String name;
	
	private MediaType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	/**
	 * @Title: 根据名称获取会议配置类型 
	 * @param name 名称
	 * @throws Exception 
	 * @return ConfigType 会议配置类型
	 */
	public static MediaType fromName(String name) throws Exception{
		MediaType[] values = MediaType.values();
		for(MediaType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
