package com.sumavision.bvc.BO;

import java.util.List;


import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class CombineVideoDecodeChannelParam {
	
	private String method;
	
	private String seq;
	
	private String bundleID;
	
	private String channelID;
	
	private String mediaType = "videoDecode";
	
	private String chlType;
	
	private Integer interval;
	
	private List<Src> srcArray;
	
	
	@Getter
	@Setter
	public static class Src{
		
		private String layerID;
		
		private String bundleID;
		
		private String channelID;
	}
	
	public enum MethodContent{
		OpenChannel,
		ModifyChannel,
		CloseChannel
	}
	
	public enum ChannelType{
		Single,
		Loop
	}
	
}
