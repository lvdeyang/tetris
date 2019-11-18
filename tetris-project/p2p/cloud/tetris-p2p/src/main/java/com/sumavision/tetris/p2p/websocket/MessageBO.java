package com.sumavision.tetris.p2p.websocket;

public class MessageBO {

	/** 消息去向websocket的key */
	private String key;
	
	/** 消息内容 */
	private String message;

	public String getKey() {
		return key;
	}

	public MessageBO setKey(String key) {
		this.key = key;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public MessageBO setMessage(String message) {
		this.message = message;
		return this;
	}
	
}
