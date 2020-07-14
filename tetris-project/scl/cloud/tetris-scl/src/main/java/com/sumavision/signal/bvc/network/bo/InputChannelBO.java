package com.sumavision.signal.bvc.network.bo;

/**
 * ChannelBO中没有bundleId，业务需要bundleId<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年3月24日 下午4:10:03
 */
public class InputChannelBO {

	private String layerId;
	
	private String bundleId;
	
	private String channelId;

	public String getLayerId() {
		return layerId;
	}

	public InputChannelBO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public InputChannelBO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getChannelId() {
		return channelId;
	}

	public InputChannelBO setChannelId(String channelId) {
		this.channelId = channelId;
		return this;
	}
	
}
