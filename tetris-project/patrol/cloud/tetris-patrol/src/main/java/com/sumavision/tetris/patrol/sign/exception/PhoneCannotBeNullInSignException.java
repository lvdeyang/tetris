package com.sumavision.tetris.patrol.sign.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class PhoneCannotBeNullInSignException extends BaseException{

	private static final long serialVersionUID = 1L;

	public PhoneCannotBeNullInSignException() {
		super(StatusCode.FORBIDDEN, "请填写手机号码！");
	}

}
