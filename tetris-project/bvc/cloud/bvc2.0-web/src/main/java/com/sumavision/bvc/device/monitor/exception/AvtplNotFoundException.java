package com.sumavision.bvc.device.monitor.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class AvtplNotFoundException extends BaseException{

	private static final long serialVersionUID = 1L;

	public AvtplNotFoundException(String message) {
		super(StatusCode.FORBIDDEN, message);
	}

}
