package com.suma.venus.message.bo;

import java.util.UUID;

import com.suma.venus.message.bo.VenusMessageHead.MsgType;
import com.suma.venus.message.mq.VenusMessage;

public class NotifyBaseBO {

	private VenusMessage message = new VenusMessage();

	public NotifyBaseBO() {
		this.getMessage().getMessage_header().setMessage_type(MsgType.notification.toString());
		this.getMessage().getMessage_header().setSequence_id(UUID.randomUUID().toString());
	}

	public VenusMessage getMessage() {
		return message;
	}

	public void setMessage(VenusMessage message) {
		this.message = message;
	}
}
