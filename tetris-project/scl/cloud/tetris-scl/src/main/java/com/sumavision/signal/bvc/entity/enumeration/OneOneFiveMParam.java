package com.sumavision.signal.bvc.entity.enumeration;

import com.sumavision.tetris.orm.exception.ErrorTypeException;

/**
 * 115M设备参数枚举<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年12月16日 下午2:39:07
 */
public enum OneOneFiveMParam {

	//视频编码类型
	H264("h264", 1l),
	H265("h265", 0l),
	
	//视频分辨率
	P3840x2160F5994("3840x2160P59.94", 1l),
	P3840x2160F60("3840x2160P60.0", 2l),
	P3840x2160F50("3840x2160P50.0", 3l),
	P3840x2160F2997("3840x2160P29.97", 4l),
	P3840x2160F30("3840x2160P30.0", 5l),
	P3840x2160F25("3840x2160P25.0", 6l),
	P3840x2160F2398("3840x2160P23.98", 7l),
	P3840x2160F24("3840x2160P24.0", 8l),
	P1920x1080F5994("1920x1080P59.94", 16l),
	P1920x1080F60("1920x1080P60.0", 17l),
	P1920x1080F50("1920x1080P50.0", 18l),
	P1920x1080F2997("1920x1080P29.97", 19l),
	P1920x1080F30("1920x1080P30.0", 20l),
	P1920x1080F2398("1920x1080P23.98", 21l),
	P1920x1080F24("1920x1080P24.0", 22l),
	P1920x1080F25("1920x1080P25.0", 23l),
	P1280x720F5994("1280x720P59.94", 48l),
	P1280x720F60("1280x720P60.0", 49l),
	P1280x720F50("1280x720P50.0", 50l),
	P1280x720F2997("1280x720P2997", 51l),
	P1280x720F30("1280x720P30.0", 52l),
	P1280x720F25("1280x720P25.0", 53l),
	P1280x720F2398("1280x720P2398", 54l),
	P1280x720F24("1280x720P24.0", 55l),
	
	//音频编码
	AAC_LC("aac", 0l),
	
	//音频采样率
	SR480("48000sr", 0l),
	SR441("44100sr", 1l),
	SR320("32000sr", 2l),
	
	//音频码率
	ABR32("32000abr", 0l),
	ABR48("48000abr", 1l),
	ABR56("56000abr", 2l),
	ABR64("64000abr", 3l),
	ABR80("80000abr", 4l),
	ABR96("96000abr", 5l),
	ABR112("112000abr", 6l),
	ABR128("128000abr", 7l),
	ABR160("160000abr", 8l),
	ABR192("192000abr", 9l),
	ABR224("224000abr", 10l),
	ABR256("256000abr", 11l),
	ABR320("320000abr", 12l),
	ABR384("384000abr", 13l);
	
	private String name;
	
	private Long protocal;
	
	private OneOneFiveMParam(String name, Long protocal){
		this.name = name;
		this.protocal = protocal;
	}

	public String getName() {
		return name;
	}

	public Long getProtocal() {
		return protocal;
	}
	
	public static OneOneFiveMParam fromName(String name) throws Exception{
		OneOneFiveMParam[] values = OneOneFiveMParam.values();
		for(OneOneFiveMParam value: values){
			if(value.getName().equals(name)){
				return value;
			}
		}
		throw new ErrorTypeException("name", name);
	}
	
}
