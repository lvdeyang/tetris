package com.sumavision.tetris.websocket.message;

import com.sumavision.tetris.commons.util.date.DateUtil;

public class WebsocketMessageVO{

	private Long id;
	
	private String uuid;
	
	private String updateTime;
	
	private Long userId;
	
	private String username;
	
	private String message;
	
	private String messageType;
	
	private boolean consumed;
	
	private Long fromUserId;
	
	private String fromUsername;

	public Long getId() {
		return id;
	}

	public WebsocketMessageVO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public WebsocketMessageVO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public WebsocketMessageVO setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public Long getUserId() {
		return userId;
	}

	public WebsocketMessageVO setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	public String getUsername() {
		return username;
	}

	public WebsocketMessageVO setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public WebsocketMessageVO setMessage(String message) {
		this.message = message;
		return this;
	}

	public String getMessageType() {
		return messageType;
	}

	public WebsocketMessageVO setMessageType(String messageType) {
		this.messageType = messageType;
		return this;
	}

	public boolean isConsumed() {
		return consumed;
	}

	public WebsocketMessageVO setConsumed(boolean consumed) {
		this.consumed = consumed;
		return this;
	}

	public Long getFromUserId() {
		return fromUserId;
	}

	public WebsocketMessageVO setFromUserId(Long fromUserId) {
		this.fromUserId = fromUserId;
		return this;
	}

	public String getFromUsername() {
		return fromUsername;
	}

	public WebsocketMessageVO setFromUsername(String fromUsername) {
		this.fromUsername = fromUsername;
		return this;
	}
	
	public WebsocketMessageVO set(WebsocketMessagePO entity) throws Exception{
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setUserId(entity.getUserId())
			.setUsername(entity.getUsername())
			.setMessage(entity.getMessage())
			.setMessageType(entity.getMessageType().toString())
			.setConsumed(entity.isConsumed())
			.setFromUserId(entity.getFromUserId())
			.setFromUsername(entity.getFromUsername());
		return this;
	}
	
}
