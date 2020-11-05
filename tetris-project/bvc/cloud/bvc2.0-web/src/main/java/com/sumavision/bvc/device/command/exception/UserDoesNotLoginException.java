package com.sumavision.bvc.device.command.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class UserDoesNotLoginException extends BaseException{

	private static final long serialVersionUID = 1L;

	public UserDoesNotLoginException(String userName) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("被呼用户未登录，用户名为：")
															 .append(userName)
															 .toString());
	}

}
