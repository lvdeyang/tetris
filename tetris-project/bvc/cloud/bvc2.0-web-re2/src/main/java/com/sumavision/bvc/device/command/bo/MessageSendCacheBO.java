package com.sumavision.bvc.device.command.bo;

import com.sumavision.tetris.websocket.message.WebsocketMessageType;

import lombok.Getter;
import lombok.Setter;

/**
 * 
* @ClassName: MessageSendCacheBO 
* @Description: 用于暂存发送消息的信息，留待后边一起发送
* @author zsy
* @date 2019年11月18日 下午1:14:15 
*
 */
@Getter
@Setter
public class MessageSendCacheBO {

	private Long userId;
	private String message;
	private WebsocketMessageType type;
	private Long fromUserId;
	private String fromUsername;
	
	public MessageSendCacheBO(){}
	
	public MessageSendCacheBO(
			Long userId,
			String message,
			WebsocketMessageType type){
		this.userId = userId;
		this.message = message;
		this.type = type;
	}
	
	/** 后边的两个参数已经弃用 */
	public MessageSendCacheBO(
			Long userId,
			String message,
			WebsocketMessageType type,
			Long fromUserId,
			String fromUsername){
		this.userId = userId;
		this.message = message;
		this.type = type;
		this.fromUserId = fromUserId;
		this.fromUsername = fromUsername;
	}

}
