package com.sumavision.tetris.bvc.model.terminal.channel;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum TerminalChannelType {

	VIDEO_ENCODE("视频编码"),
	AUDIO_ENCODE("音频编码"),
	VIDEO_DECODE("视频解码"),
	AUDIO_DECODE("音频解码");
	
	private String name;
	
	private TerminalChannelType(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static TerminalChannelType fromName(String name) throws Exception{
		TerminalChannelType[] values = TerminalChannelType.values();
		for(TerminalChannelType value:values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
