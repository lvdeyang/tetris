package com.sumavision.tetris.zoom.call.user.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class CallUserNotExistException extends BaseException{

	private static final long serialVersionUID = 1L;

	public CallUserNotExistException(Long id) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("当前呼叫已停止，id：").append(id).toString());
	}

}
