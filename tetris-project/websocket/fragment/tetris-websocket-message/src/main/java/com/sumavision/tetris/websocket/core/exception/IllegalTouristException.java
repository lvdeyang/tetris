package com.sumavision.tetris.websocket.core.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class IllegalTouristException extends BaseException{

	private static final long serialVersionUID = 1L;

	public IllegalTouristException() {
		super(StatusCode.FORBIDDEN, "非法的游客！");
	}

}
