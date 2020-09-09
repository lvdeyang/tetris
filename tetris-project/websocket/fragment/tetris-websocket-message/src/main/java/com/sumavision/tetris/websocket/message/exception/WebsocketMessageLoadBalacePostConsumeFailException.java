package com.sumavision.tetris.websocket.message.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class WebsocketMessageLoadBalacePostConsumeFailException extends BaseException{

	private static final long serialVersionUID = 1L;

	public WebsocketMessageLoadBalacePostConsumeFailException(String ip, String port, Long id) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("websocket负载均衡消费消息失败！目标服务：")
															 .append(ip).append(":").append(port)
															 .append("，消息id：").append(id)
															 .toString());
	}
	
}
