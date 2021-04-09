package com.sumavision.bvc.BO;

import com.suma.venus.message.bo.RequestBaseBO;

public class CloseCombineVideoDecodeChannelRequest extends RequestBaseBO {

	private CombineVideoDecodeChannelParam close_channel_request;

	public CombineVideoDecodeChannelParam getClose_channel_request() {
		return close_channel_request;
	}

	public void setClose_channel_request(CombineVideoDecodeChannelParam close_channel_request) {
		this.close_channel_request = close_channel_request;
	}

	
	
	
}
