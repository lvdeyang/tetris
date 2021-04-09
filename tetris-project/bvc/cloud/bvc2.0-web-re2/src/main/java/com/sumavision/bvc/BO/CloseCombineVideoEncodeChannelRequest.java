package com.sumavision.bvc.BO;

import com.suma.venus.message.bo.RequestBaseBO;

public class CloseCombineVideoEncodeChannelRequest extends RequestBaseBO {

	private CombineVideoEncodeChannelParam close_channel_request;

	public CombineVideoEncodeChannelParam getClose_channel_request() {
		return close_channel_request;
	}

	public void setClose_channel_request(CombineVideoEncodeChannelParam close_channel_request) {
		this.close_channel_request = close_channel_request;
	}				
}
