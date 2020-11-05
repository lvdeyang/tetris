package com.sumavision.tetris.cs.channel.broad.ability.transcode;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum BroadTranscodeType {
	MEDIA_EDITO("云转码"),
	FILE_STREAM_TRANSCODE("文件转流转码"),//*
	STREAM_TRANSCODE("流转码");
	
	private String name;

	public String getName() {
		return name;
	}
	
	private BroadTranscodeType(String name) {
		this.name = name;
	}
	
	public BroadTranscodeType fromName(String name) throws Exception {
		BroadTranscodeType[] values = BroadTranscodeType.values();
		for (BroadTranscodeType value: values) {
			if (value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("cs转码类型", name);
	}
}
