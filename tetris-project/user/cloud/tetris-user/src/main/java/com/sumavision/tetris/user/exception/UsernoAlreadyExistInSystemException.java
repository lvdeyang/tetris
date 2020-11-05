package com.sumavision.tetris.user.exception;

import java.util.Collection;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class UsernoAlreadyExistInSystemException extends BaseException{

	private static final long serialVersionUID = 1L;

	public UsernoAlreadyExistInSystemException(Collection<String> usernos) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("用户号码在系统中已经存在！").append(usernos.toString().replace("[", "").replace("]", "")).toString());
	}
	
}
