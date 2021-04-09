package com.sumavision.bvc.BO;

import java.util.List;


import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class CombineVideoEncodeChannelParam {
	
	private String method;
	
	private String seq;
	
	private String bundleID;
	
	private String channelID;
	
	private String mediaType = "videoMixer";
	
	private String codec;
	
	private Integer width;
	
	private Integer height;
	
	//编码档次，h264取值为：main，base，high
	private String profile;
	
	//输出码率，单位为bps
	private Integer bitrate;
	
	private List<ScreenLayerSrc> screenLayers;
	
	@Getter
	@Setter
	public static class ScreenLayerSrc{
		
		private String x;
		
		private String y;
		
		private String width;
		
		private String height;
		
		private Integer zindex = 0;
		
		private String srcBundleID;
		
		private String srcChannelID;
	}
	
	public enum CodecProfile{
		base,
		main,
		hign
	}
	
	
	
}
