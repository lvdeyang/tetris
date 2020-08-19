package com.sumavision.bvc.device.monitor.mux.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class BusinessInvokingException extends BaseException{

	private static final long serialVersionUID = 1L;

	public BusinessInvokingException() {
		super(StatusCode.FORBIDDEN, "系统中有业务运行，无法做端口复用模式切换！");
	}

}
