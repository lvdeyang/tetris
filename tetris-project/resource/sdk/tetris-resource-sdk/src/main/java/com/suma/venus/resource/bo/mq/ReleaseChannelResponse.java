package com.suma.venus.resource.bo.mq;

import com.suma.venus.resource.base.bo.ReleaseChannelResponseBody;

public class ReleaseChannelResponse{
	
	private ReleaseChannelResponseBody release_channel_response;
	
	public ReleaseChannelResponse() {
		super();
	}

	public ReleaseChannelResponse(ReleaseChannelResponseBody release_channel_response) {
		this.release_channel_response = release_channel_response;
	}

	public ReleaseChannelResponseBody getRelease_channel_response() {
		return release_channel_response;
	}

	public void setRelease_channel_response(
			ReleaseChannelResponseBody release_channel_response) {
		this.release_channel_response = release_channel_response;
	}
	
}
