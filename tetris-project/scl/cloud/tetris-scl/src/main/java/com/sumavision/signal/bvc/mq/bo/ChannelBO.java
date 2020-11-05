package com.sumavision.signal.bvc.mq.bo;

public class ChannelBO {
	
	private String channel_id;
	
	private String channel_status;
	
	private ChannelParamBO channel_param;

	public String getChannel_id() {
		return channel_id;
	}

	public void setChannel_id(String channel_id) {
		this.channel_id = channel_id;
	}

	public String getChannel_status() {
		return channel_status;
	}

	public void setChannel_status(String channel_status) {
		this.channel_status = channel_status;
	}

	public ChannelParamBO getChannel_param() {
		return channel_param;
	}

	public void setChannel_param(ChannelParamBO channel_param) {
		this.channel_param = channel_param;
	}
}
