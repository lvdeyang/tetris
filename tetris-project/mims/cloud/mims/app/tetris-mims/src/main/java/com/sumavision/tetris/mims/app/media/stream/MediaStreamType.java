package com.sumavision.tetris.mims.app.media.stream;

import java.util.ArrayList;
import java.util.List;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

public enum MediaStreamType {
	UDP_TS("udp_ts"),
	RTP_TS("rtp_ts"),
	HTTP_TS("http_ts"),
	SRT_TS("srt_ts"),
	HLS("hls"),
	DASH("dash"),
	MSS("mss"),
	RTSP("rtsp"),
	RTMP("rtmp"),
	HTTP_FLV("http_flv"),
	SDI("sdi"),
	ZI_XI("zixi");
	
	private String name;

	public String getName() {
		return name;
	}

	private MediaStreamType(String name) {
		this.name = name;
	}
	
	public static MediaStreamType fromName(String name) throws Exception {
		MediaStreamType[] values = MediaStreamType.values();
		for (MediaStreamType mediaStreamType : values) {
			if (mediaStreamType.getName().equals(name)) {
				return mediaStreamType;
			}
		}
		throw new ErrorTypeException("streamType", name);
	}
	
	/**
	 * 获取流类型String数组<br/>
	 * <b>作者:</b>lzp<br/>
	 * <b>版本：</b>1.0<br/>
	 * <b>日期：</b>2020年3月4日 上午11:49:04
	 * @return List<String> 流类型数组
	 */
	public static List<String> queryAllType() throws Exception {
		List<String> types = new ArrayList<String>();
		MediaStreamType[] streamTypes = MediaStreamType.values();
		for (MediaStreamType mediaStreamType : streamTypes) {
			types.add(mediaStreamType.getName());
		}
		return types;
	}
}
