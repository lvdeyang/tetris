package com.sumavision.tetris.user.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class PasswordComplexityException extends BaseException{

	private static final long serialVersionUID = 1L;

	public PasswordComplexityException(String message) {
		super(StatusCode.FORBIDDEN, message);
	}

}
