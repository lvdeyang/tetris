package com.suma.venus.resource.bo.mq;

import com.suma.venus.resource.base.bo.ResponseBody;

public class LockChannelResp {

	private ResponseBody lock_channel_response;
	
	public LockChannelResp() {
	}

	public LockChannelResp(ResponseBody lock_channel_response) {
		this.lock_channel_response = lock_channel_response;
	}

	public ResponseBody getLock_channel_response() {
		return lock_channel_response;
	}

	public void setLock_channel_response(ResponseBody lock_channel_response) {
		this.lock_channel_response = lock_channel_response;
	}
	
}
