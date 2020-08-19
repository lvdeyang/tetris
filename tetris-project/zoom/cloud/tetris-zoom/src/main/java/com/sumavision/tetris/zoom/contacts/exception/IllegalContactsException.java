package com.sumavision.tetris.zoom.contacts.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class IllegalContactsException extends BaseException{

	private static final long serialVersionUID = 1L;

	public IllegalContactsException(String message) {
		super(StatusCode.FORBIDDEN, message);
	}

}
