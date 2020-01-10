package com.suma.venus.message.mq;

import com.alibaba.fastjson.JSONObject;
import com.suma.venus.message.bo.VenusMessageHead;

public class VenusMessage {
	
	protected VenusMessageHead message_header = new VenusMessageHead();
	
	protected JSONObject message_body = new JSONObject();

	public VenusMessageHead getMessage_header() {
		return message_header;
	}

	public void setMessage_header(VenusMessageHead message_header) {
		this.message_header = message_header;
	}

	public JSONObject getMessage_body() {
		return message_body;
	}

	public void setMessage_body(JSONObject message_body) {
		this.message_body = message_body;
	}
	
}
