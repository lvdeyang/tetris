package com.sumavision.bvc.device.monitor.message.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class MessageAlreadyTimeoutException extends BaseException{

	private static final long serialVersionUID = 1L;

	public MessageAlreadyTimeoutException() {
		super(StatusCode.FORBIDDEN, "当前消息已经超时被挂断");
	}
	
}
