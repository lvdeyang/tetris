/**
 * 
 */
package com.sumavision.tetris.cs.channel;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 类型概述<br/>
 * <b>作者:</b>zhouaining<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年12月16日 下午2:04:35
 */
/**mpeg2,h264,h265,avs2,videopassby*/
/**视频编码格式*/
public enum VideoCodec {
	MPEG2("MPEG2"),
	H264("H264"),
	H265("H265"),
	AVS2("AVS2"),
	VIDEOPASSBY("VIDEOPASSBY");
	
	private String name;
	
	public String getName() {
		return name;
	}
	
	private VideoCodec(String name){
		this.name=name;
	}
	
	public static VideoCodec fromName(String name) throws Exception{
		VideoCodec[] values = VideoCodec.values();
		for (VideoCodec value : values) {
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
}
