package com.sumavision.bvc.BO;

import com.suma.venus.message.bo.RequestBaseBO;

public class OpenCombineVideoDecodeChannelRequest extends RequestBaseBO {

	private CombineVideoDecodeChannelParam open_channel_request;

	public CombineVideoDecodeChannelParam getOpen_channel_request() {
		return open_channel_request;
	}

	public void setOpen_channel_request(CombineVideoDecodeChannelParam open_channel_request) {
		this.open_channel_request = open_channel_request;
	}
	
	
}
