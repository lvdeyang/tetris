package com.suma.venus.resource.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.annotations.Type;

/**
 * mq消息PO(用来记录收到的响应消息，处理完会删除)
 * 
 * @author lxw
 *
 */
@Entity
public class MQMessagePO extends CommonPO<MQMessagePO>{

	/**对应的请求消息ID*/
	private String requestId;
	
	private String textMessage;

	@Column(unique=true)
	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	@Type(type="text")
	@Column(name="text_message",length=20000)
	public String getTextMessage() {
		return textMessage;
	}

	public void setTextMessage(String textMessage) {
		this.textMessage = textMessage;
	}
	
}
