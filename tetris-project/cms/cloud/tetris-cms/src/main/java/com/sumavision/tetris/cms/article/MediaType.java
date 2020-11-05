package com.sumavision.tetris.cms.article;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum MediaType {

	PICTURE("图片"),
	AUDIO("音频"),
	VIDEO("视频"),
	AUDIO_STREAM("音频流"),
	VIDEO_STREAM("视频流"),
	TEXT("文本");
	
	private String name;
	
	private MediaType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
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
