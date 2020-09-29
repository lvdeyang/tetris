package com.sumavision.tetris.patrol.sign.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class NameCannotBeNullInSignException extends BaseException{

	private static final long serialVersionUID = 1L;

	public NameCannotBeNullInSignException() {
		super(StatusCode.FORBIDDEN, "请填写姓名！");
	}
	
}
