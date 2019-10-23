package com.sumavision.tetris.websocket.message;

public class StatisticsInstantMessageResultDTO {

	/** 发送用户id */
	private Long fromUserId;
	
	/** 发送用户名 */
	private String fromUsername;
	
	/** 消息数量 */
	private Long messageNumber;
	
	public StatisticsInstantMessageResultDTO(Long fromUserId, String fromUsername, Long messageNumber){
		this.fromUserId = fromUserId;
		this.fromUsername = fromUsername;
		this.messageNumber = messageNumber;
	}

	public Long getFromUserId() {
		return fromUserId;
	}

	public StatisticsInstantMessageResultDTO setFromUserId(Long fromUserId) {
		this.fromUserId = fromUserId;
		return this;
	}

	public String getFromUsername() {
		return fromUsername;
	}

	public StatisticsInstantMessageResultDTO setFromUsername(String fromUsername) {
		this.fromUsername = fromUsername;
		return this;
	}

	public Long getMessageNumber() {
		return messageNumber;
	}

	public StatisticsInstantMessageResultDTO setMessageNumber(Long messageNumber) {
		this.messageNumber = messageNumber;
		return this;
	}
	
}
