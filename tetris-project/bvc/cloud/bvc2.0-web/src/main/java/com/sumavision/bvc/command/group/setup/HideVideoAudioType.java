package com.sumavision.bvc.command.group.setup;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * @ClassName: 隐音视频类型
 * @author zsy
 * @date 2019年9月25日
 */
public enum HideVideoAudioType {

	HIDE_VIDEO_AUDIO("隐音视频"),
	HIDE_AUDIO("隐声音");
	
	private String name;
	
	private HideVideoAudioType(String name){
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
	public static HideVideoAudioType fromName(String name) throws Exception{
		HideVideoAudioType[] values = HideVideoAudioType.values();
		for(HideVideoAudioType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
