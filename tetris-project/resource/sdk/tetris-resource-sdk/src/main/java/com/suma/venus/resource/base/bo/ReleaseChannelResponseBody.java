package com.suma.venus.resource.base.bo;

import java.util.List;

public class ReleaseChannelResponseBody extends ResponseBody{

	private List<ChannelStatusParam> channels;
	
	public ReleaseChannelResponseBody() {
		super();
	}

	public ReleaseChannelResponseBody(String result) {
		super(result);
	}
	
	public ReleaseChannelResponseBody(String result, Integer errorCode) {
		super(result, errorCode);
	}

	public List<ChannelStatusParam> getChannels() {
		return channels;
	}

	public void setChannels(List<ChannelStatusParam> channels) {
		this.channels = channels;
	}
	
}
