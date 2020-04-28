package com.sumavision.tetris.user.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class UsernoComplexityException extends BaseException{

	private static final long serialVersionUID = 1L;

	public UsernoComplexityException(String userno) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("用户号码必须为11位数字！").append(userno).toString());
	}

}
