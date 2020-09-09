package com.sumavision.tetris.websocket.message.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class WebsocketMessageLoadBalacePostSendFailException extends BaseException{

	private static final long serialVersionUID = 1L;

	public WebsocketMessageLoadBalacePostSendFailException(String ip, String port, String fromUsername, String toUsername, String message) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("websocket负载均衡推送消息失败！目标服务：")
															 .append(ip).append(":").append(port)
															 .append("， 发送用户：").append(fromUsername)
															 .append("，目标用户：").append(toUsername)
															 .append("，消息内容：").append(message)
															 .toString());
	}
	
	public WebsocketMessageLoadBalacePostSendFailException(String ip, String port, Long messageId) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("websocket负载均衡重发消息失败！目标服务：")
															 .append(ip).append(":").append(port)
															 .append("， 消息id：").append(messageId)
															 .toString());
	}
	
}
