package com.sumavision.bvc.basic.bo;

public class BusinessBundleBO {

	private String bundleId;
	
	private String bundleName;
	
	private String layerId;
	
	private String bundleType;
	
	//视频编码通道id
	private String encodeVideoChannelId;
	
	//视频编码通道类型
	private String encodeVideoChannelType;
	
	//音频编码通道id
	private String encodeAudioChannelId;
	
	//音频编码通道类型
	private String encodeAudioChannelType;
	
	//音频解码通道id
	private String decodeAudioChannelId;
	
	//音频解码通道类型
	private String decodeAudioChannelType;
	
	//视频解码通道id
	private String decodeVideoChannelId;
	
	//视频解码通道类型
	private String decodeVideoChannelType;

	public String getBundleId() {
		return bundleId;
	}

	public BusinessBundleBO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getBundleName() {
		return bundleName;
	}

	public BusinessBundleBO setBundleName(String bundleName) {
		this.bundleName = bundleName;
		return this;
	}

	public String getLayerId() {
		return layerId;
	}

	public BusinessBundleBO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	public String getEncodeVideoChannelId() {
		return encodeVideoChannelId;
	}

	public BusinessBundleBO setEncodeVideoChannelId(String encodeVideoChannelId) {
		this.encodeVideoChannelId = encodeVideoChannelId;
		return this;
	}

	public String getEncodeAudioChannelId() {
		return encodeAudioChannelId;
	}

	public BusinessBundleBO setEncodeAudioChannelId(String encodeAudioChannelId) {
		this.encodeAudioChannelId = encodeAudioChannelId;
		return this;
	}

	public String getDecodeAudioChannelId() {
		return decodeAudioChannelId;
	}

	public BusinessBundleBO setDecodeAudioChannelId(String decodeAudioChannelId) {
		this.decodeAudioChannelId = decodeAudioChannelId;
		return this;
	}

	public String getDecodeVideoChannelId() {
		return decodeVideoChannelId;
	}

	public BusinessBundleBO setDecodeVideoChannelId(String decodeVideoChannelId) {
		this.decodeVideoChannelId = decodeVideoChannelId;
		return this;
	}

	public String getBundleType() {
		return bundleType;
	}

	public BusinessBundleBO setBundleType(String bundleType) {
		this.bundleType = bundleType;
		return this;
	}

	public String getEncodeVideoChannelType() {
		return encodeVideoChannelType;
	}

	public BusinessBundleBO setEncodeVideoChannelType(String encodeVideoChannelType) {
		this.encodeVideoChannelType = encodeVideoChannelType;
		return this;
	}

	public String getEncodeAudioChannelType() {
		return encodeAudioChannelType;
	}

	public BusinessBundleBO setEncodeAudioChannelType(String encodeAudioChannelType) {
		this.encodeAudioChannelType = encodeAudioChannelType;
		return this;
	}

	public String getDecodeAudioChannelType() {
		return decodeAudioChannelType;
	}

	public BusinessBundleBO setDecodeAudioChannelType(String decodeAudioChannelType) {
		this.decodeAudioChannelType = decodeAudioChannelType;
		return this;
	}

	public String getDecodeVideoChannelType() {
		return decodeVideoChannelType;
	}

	public BusinessBundleBO setDecodeVideoChannelType(String decodeVideoChannelType) {
		this.decodeVideoChannelType = decodeVideoChannelType;
		return this;
	}
	
}
