package com.suma.venus.resource.service.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class SystemNodeNotFoundException extends BaseException{

	/** 这是一个常量的说明 */
	private static final long serialVersionUID = 1L;

	public SystemNodeNotFoundException(String message) {
		super(StatusCode.FORBIDDEN, message);
	}
	
}
