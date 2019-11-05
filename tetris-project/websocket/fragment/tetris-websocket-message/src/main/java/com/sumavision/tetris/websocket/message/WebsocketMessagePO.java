package com.sumavision.tetris.websocket.message;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_WEBSOCKET_MESSAGE")
public class WebsocketMessagePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 目标用户id */
	private Long userId;
	
	/** 目标用户名称 */
	private String username;
	
	/** 推送内容 */
	private String message;
	
	/** 推送消息类型 */
	private WebsocketMessageType messageType;
	
	/** 消息是否被消费 */
	private boolean consumed;
	
	/** 消息来源用户id */
	private Long fromUserId;
	
	/** 消息来源用户名称 */
	private String fromUsername;

	@Column(name = "USER_ID")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "USERNAME")
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Lob
	@Column(name = "MESSAGE", columnDefinition = "LONGTEXT")
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "MESSAGE_TYPE")
	public WebsocketMessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(WebsocketMessageType messageType) {
		this.messageType = messageType;
	}

	@Column(name = "CONSUMED")
	public boolean isConsumed() {
		return consumed;
	}

	public void setConsumed(boolean consumed) {
		this.consumed = consumed;
	}

	@Column(name = "FROM_USER_ID")
	public Long getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(Long fromUserId) {
		this.fromUserId = fromUserId;
	}

	@Column(name = "FROM_USERNAME")
	public String getFromUsername() {
		return fromUsername;
	}

	public void setFromUsername(String fromUsername) {
		this.fromUsername = fromUsername;
	}
	
}
