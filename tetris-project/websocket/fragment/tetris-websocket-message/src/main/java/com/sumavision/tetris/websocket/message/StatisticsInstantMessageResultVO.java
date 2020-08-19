package com.sumavision.tetris.websocket.message;

public class StatisticsInstantMessageResultVO {

	/** 发送用户id */
	private Long fromUserId;
	
	/** 发送用户名 */
	private String fromUsername;
	
	/** 消息数量 */
	private Long messageNumber;

	public Long getFromUserId() {
		return fromUserId;
	}

	public StatisticsInstantMessageResultVO setFromUserId(Long fromUserId) {
		this.fromUserId = fromUserId;
		return this;
	}

	public String getFromUsername() {
		return fromUsername;
	}

	public StatisticsInstantMessageResultVO setFromUsername(String fromUsername) {
		this.fromUsername = fromUsername;
		return this;
	}

	public Long getMessageNumber() {
		return messageNumber;
	}

	public StatisticsInstantMessageResultVO setMessageNumber(Long messageNumber) {
		this.messageNumber = messageNumber;
		return this;
	}
	
	public StatisticsInstantMessageResultVO set(StatisticsInstantMessageResultDTO entity) throws Exception{
		this.setFromUserId(entity.getFromUserId())
			.setFromUsername(entity.getFromUsername())
			.setMessageNumber(entity.getMessageNumber());
		return this;
	}
	
}
