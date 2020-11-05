package com.sumavision.bvc.basic.bo;

/**
 * 业务转发BO，可直接生成协议<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月14日 下午2:38:48
 */
public class BusinessForwardBO {

	/*****************
	 * 以下描述源
	 *****************/
	
	/** 合屏或混音，合屏【|混音】uuid */
	private String sourceCombineUuid;
	
	/** 通道，来自于资源层 */
	private String sourceLayerId;
	
	/** 通道，来自于资源层：源的设备id */
	private String sourceBundleId;
	
	/** 通道，来自于资源层：源的设备id */
	private String sourceBundleName;
	
	/** 通道，来自于资源层：源的通道id */
	private String sourceChannelId;
	
	/*****************
	 * 以下描述目的
	 *****************/
	
	/** 来自于资源层 */
	private String layerId;
	
	/** 真实的设备id（来自资源层） */
	private String bundleId;
	
	/** 真实的设备名称 （来自资源层）*/
	private String bundleName;
	
	/** 真实设备类型 （来自资源层） */
	private String venusBundleType;
	
	/** 真实的通道id（来自于资源层） */
	private String channelId;
	
	/** 资源层的channelName */
	private String channelType;

	public String getSourceCombineUuid() {
		return sourceCombineUuid;
	}

	public BusinessForwardBO setSourceCombineUuid(String sourceCombineUuid) {
		this.sourceCombineUuid = sourceCombineUuid;
		return this;
	}

	public String getSourceLayerId() {
		return sourceLayerId;
	}

	public BusinessForwardBO setSourceLayerId(String sourceLayerId) {
		this.sourceLayerId = sourceLayerId;
		return this;
	}

	public String getSourceBundleId() {
		return sourceBundleId;
	}

	public BusinessForwardBO setSourceBundleId(String sourceBundleId) {
		this.sourceBundleId = sourceBundleId;
		return this;
	}

	public String getSourceBundleName() {
		return sourceBundleName;
	}

	public BusinessForwardBO setSourceBundleName(String sourceBundleName) {
		this.sourceBundleName = sourceBundleName;
		return this;
	}

	public String getSourceChannelId() {
		return sourceChannelId;
	}

	public BusinessForwardBO setSourceChannelId(String sourceChannelId) {
		this.sourceChannelId = sourceChannelId;
		return this;
	}

	public String getLayerId() {
		return layerId;
	}

	public BusinessForwardBO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public BusinessForwardBO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getBundleName() {
		return bundleName;
	}

	public BusinessForwardBO setBundleName(String bundleName) {
		this.bundleName = bundleName;
		return this;
	}

	public String getVenusBundleType() {
		return venusBundleType;
	}

	public BusinessForwardBO setVenusBundleType(String venusBundleType) {
		this.venusBundleType = venusBundleType;
		return this;
	}

	public String getChannelId() {
		return channelId;
	}

	public BusinessForwardBO setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}

	public String getChannelType() {
		return channelType;
	}

	public BusinessForwardBO setChannelType(String channelType) {
		this.channelType = channelType;
		return this;
	}
	
}
