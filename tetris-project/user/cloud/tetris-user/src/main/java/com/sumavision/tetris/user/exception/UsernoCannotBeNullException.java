package com.sumavision.tetris.user.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class UsernoCannotBeNullException extends BaseException{

	private static final long serialVersionUID = 1L;

	public UsernoCannotBeNullException(String nickname) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("用户：").append(nickname).append("号码为空！").toString());
	}

}
