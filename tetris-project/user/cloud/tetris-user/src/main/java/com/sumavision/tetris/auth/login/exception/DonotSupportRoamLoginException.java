package com.sumavision.tetris.auth.login.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class DonotSupportRoamLoginException extends BaseException{

	private static final long serialVersionUID = 1L;

	public DonotSupportRoamLoginException() {
		super(StatusCode.FORBIDDEN, "暂时不支持漫游登录");
	}

}
