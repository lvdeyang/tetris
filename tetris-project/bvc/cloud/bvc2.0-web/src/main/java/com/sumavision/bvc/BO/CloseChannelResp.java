package com.sumavision.bvc.BO;

import com.suma.venus.message.mq.ResponseBO;

public class CloseChannelResp extends ResponseBO{

	private ResponseBody close_channel_response;
	
	public CloseChannelResp() {
		super();
	}

	public CloseChannelResp(int result, String dstID, String requestID,ResponseBody close_channel_response) {
		super(result, dstID, requestID);
		this.close_channel_response = close_channel_response;
	}

	

	public ResponseBody getClose_channel_response() {
		return close_channel_response;
	}

	public void setClose_channel_response(ResponseBody close_channel_response) {
		this.close_channel_response = close_channel_response;
	}	
}
