package com.sumavision.tetris.resouce.feign.resource;

public class ResourceVO {

	/** 用户id */
	private String userId;
	
	/** 设备id */
	private String bundleId;
	
	/** 接入层id */
	private String layerId;
	
	/** 视频通道id */
	private String videoChannelId;
	
	/** 音频通道id */
	private String audioChannelId;
	
	/** 屏幕视频通道id */
	private String screenVideoChannelId;
	
	/** 屏幕音频通道id */
	private String screenAudioChannelId;
	
	/** 会议成员终端类型 */
	private String type;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	public String getLayerId() {
		return layerId;
	}

	public void setLayerId(String layerId) {
		this.layerId = layerId;
	}

	public String getVideoChannelId() {
		return videoChannelId;
	}

	public void setVideoChannelId(String videoChannelId) {
		this.videoChannelId = videoChannelId;
	}

	public String getAudioChannelId() {
		return audioChannelId;
	}

	public void setAudioChannelId(String audioChannelId) {
		this.audioChannelId = audioChannelId;
	}

	public String getScreenVideoChannelId() {
		return screenVideoChannelId;
	}

	public void setScreenVideoChannelId(String screenVideoChannelId) {
		this.screenVideoChannelId = screenVideoChannelId;
	}

	public String getScreenAudioChannelId() {
		return screenAudioChannelId;
	}

	public void setScreenAudioChannelId(String screenAudioChannelId) {
		this.screenAudioChannelId = screenAudioChannelId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
