package com.sumavision.bvc.device.jv230.dto;

public class Jv230ChannelDTO {

	private String bundleId;
	
	private String layerId;
	
	private String channelId;

	public Jv230ChannelDTO(
			String bundleId,
			String layerId,
			String channelId){
		
		this.bundleId = bundleId;
		this.layerId = layerId;
		this.channelId = channelId;
		
	}
	
	public String getBundleId() {
		return bundleId;
	}

	public Jv230ChannelDTO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getLayerId() {
		return layerId;
	}

	public Jv230ChannelDTO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	public String getChannelId() {
		return channelId;
	}

	public Jv230ChannelDTO setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}
	
}
