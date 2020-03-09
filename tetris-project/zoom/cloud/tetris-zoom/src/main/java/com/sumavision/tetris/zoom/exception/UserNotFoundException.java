package com.sumavision.tetris.zoom.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class UserNotFoundException extends BaseException{

	private static final long serialVersionUID = 1L;

	public UserNotFoundException(Long userId) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("用户不存在！id：").append(userId).toString());
	}

}
