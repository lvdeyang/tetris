package com.sumavision.bvc.device.command.basic.osd;

public class BundleDTO {

	private String bundleId;
	
	private String layerId;
	
	private String bundleType;
	
	private String videoChannelId;
	
	private String videoChannelBaseType;

	public String getBundleId() {
		return bundleId;
	}

	public BundleDTO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getLayerId() {
		return layerId;
	}

	public BundleDTO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	public String getBundleType() {
		return bundleType;
	}

	public BundleDTO setBundleType(String bundleType) {
		this.bundleType = bundleType;
		return this;
	}

	public String getVideoChannelId() {
		return videoChannelId;
	}

	public BundleDTO setVideoChannelId(String videoChannelId) {
		this.videoChannelId = videoChannelId;
		return this;
	}

	public String getVideoChannelBaseType() {
		return videoChannelBaseType;
	}

	public BundleDTO setVideoChannelBaseType(String videoChannelBaseType) {
		this.videoChannelBaseType = videoChannelBaseType;
		return this;
	}
	
}
