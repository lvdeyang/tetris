package com.sumavision.signal.bvc.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class RepeaterResponseException extends BaseException{
	
	private static final long serialVersionUID = 1L;

	public RepeaterResponseException(String message) {
		super(StatusCode.FORBIDDEN, message);
	}

}
