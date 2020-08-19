package com.sumavision.tetris.zoom.call.user.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class CallUserBusyException extends BaseException{

	private static final long serialVersionUID = 1L;

	public CallUserBusyException() {
		super(StatusCode.FORBIDDEN, "对方正忙！");
	}

}
