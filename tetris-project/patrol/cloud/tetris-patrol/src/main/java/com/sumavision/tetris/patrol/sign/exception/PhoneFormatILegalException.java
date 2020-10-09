package com.sumavision.tetris.patrol.sign.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class PhoneFormatILegalException extends BaseException{

	private static final long serialVersionUID = 1L;

	public PhoneFormatILegalException() {
		super(StatusCode.FORBIDDEN, "请输入正确的手机号码！");
	}

}
