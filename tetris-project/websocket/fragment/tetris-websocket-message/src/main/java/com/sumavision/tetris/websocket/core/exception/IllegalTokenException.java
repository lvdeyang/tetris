package com.sumavision.tetris.websocket.core.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class IllegalTokenException extends BaseException{

	private static final long serialVersionUID = 1L;

	public IllegalTokenException(String token) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("无效的token：").append(token).toString());
	}

}
