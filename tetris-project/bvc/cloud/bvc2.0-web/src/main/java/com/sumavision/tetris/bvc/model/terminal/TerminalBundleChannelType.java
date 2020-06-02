package com.sumavision.tetris.bvc.model.terminal;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum TerminalBundleChannelType {

	VIDEO_ENCODE("视频编码"),
	AUDIO_ENCODE("音频编码"),
	VIDEO_DECODE("视频解码"),
	AUDIO_DECODE("音频解码");
	
	private String name;
	
	private TerminalBundleChannelType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static TerminalBundleChannelType fromName(String name) throws Exception{
		TerminalBundleChannelType[] values = TerminalBundleChannelType.values();
		for(TerminalBundleChannelType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
