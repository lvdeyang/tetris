package com.sumavision.tetris.auth.login.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class QtZkLoginRepeatedlyException extends BaseException{

	private static final long serialVersionUID = 1L;

	public QtZkLoginRepeatedlyException() {
		super(StatusCode.FORBIDDEN, "当前用户在线，不能重复登录！");
	}

}
