package com.sumavision.bvc.device.monitor.live.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class UserCannotBeFoundException extends BaseException{

	private static final long serialVersionUID = 1L;

	public UserCannotBeFoundException() {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("用户不存在!")
														     .toString());
	}

}
