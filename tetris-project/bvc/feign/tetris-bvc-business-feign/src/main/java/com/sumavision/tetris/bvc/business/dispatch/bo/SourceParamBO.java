package com.sumavision.tetris.bvc.business.dispatch.bo;
/**
 * 
* @ClassName: SourceParamBO 
* @Description: 
* @author zsy
* @date 2020年3月11日 上午11:14:49 
*
 */
public class SourceParamBO {
	
	/**
	 * 源的类型 channel/combineVideo/combineAudio
	 */
	private String type = "";
	
	/**
	 * 接入层Id
	 */
	private String layerId = "";
	
	/**
	 * 设备标识		
	 */
	private String bundleId = "";
	
	/**
	 * 设备能力通道ID
	 */
	private String channelId = "";
	
	/**
	 * 源的UUID，当类型为combineVideo/combineAudio并且是新建合屏/混音时使用
	 */
	private String uuid = "";

	public String getType() {
		return type;
	}

	public SourceParamBO setType(String type) {
		this.type = type;
		return this;
	}	

	public String getLayerId() {
		return layerId;
	}

	public SourceParamBO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public SourceParamBO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getChannelId() {
		return channelId;
	}

	public SourceParamBO setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public SourceParamBO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}	
	
}
