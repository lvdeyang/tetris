package com.suma.venus.message.bo;

import java.util.UUID;

import com.suma.venus.message.bo.VenusMessageHead.MsgType;
import com.suma.venus.message.mq.VenusMessage;

/**
 * 请求参数基础BO
 *
 * @author lxw 2017年11月29日
 */
public class RequestBaseBO {
	
	private VenusMessage message = new VenusMessage();
	
	public RequestBaseBO(){
		this.getMessage().getMessage_header().setMessage_type(MsgType.request.toString());
		this.getMessage().getMessage_header().setSequence_id(UUID.randomUUID().toString());
	}

	public VenusMessage getMessage() {
		return message;
	}

	public void setMessage(VenusMessage message) {
		this.message = message;
	}
}
