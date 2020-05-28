package com.sumavision.tetris.zoom.contacts.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class RepeatContactsException extends BaseException{

	private static final long serialVersionUID = 1L;

	public RepeatContactsException() {
		super(StatusCode.FORBIDDEN, "当前用户已经添加到联系人！");
	}

}
