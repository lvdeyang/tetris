package com.sumavision.tetris.cs.channel.broad.ability;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum OutputType {
	UDP_TS("UDP_TS"),
	RTMP("RTMP"),
	HTTP_TS("HTTP_TS"),
	SRT_TS("SRT_TS"),
	HLS("HLS"),
	DASH("DASH"),
	RTSP("RTSP"),
	RTP_ES("RTP_ES"),
	HTTP_FLV("HTTP_FLV"),
	HLS_RECORD("HLS_RECORD"),
	ZIXI_TS("ZIXI_TS");
	
	private String name;

	public String getName() {
		return name;
	}

	private OutputType(String name) {
		this.name = name;
	}
	public static OutputType fromName(String name) throws Exception{
		OutputType[] values = OutputType.values();
		for (OutputType codingType : values) {
			if(codingType.getName().equals(name)){
				return codingType;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}