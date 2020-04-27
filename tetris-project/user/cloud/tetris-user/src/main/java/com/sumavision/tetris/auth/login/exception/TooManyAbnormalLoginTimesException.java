package com.sumavision.tetris.auth.login.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class TooManyAbnormalLoginTimesException extends BaseException{

	private static final long serialVersionUID = 1L;

	public TooManyAbnormalLoginTimesException() {
		super(StatusCode.FORBIDDEN, "当前用户连续10次异常登录，已被锁定，请联系管理员解锁");
	}

}
