package com.sumavision.bvc.device.monitor.live.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ParamErrorException extends BaseException{

	private static final long serialVersionUID = 1L;

	public ParamErrorException(String message) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("参数不合法！")
															 .append(message)
															 .toString());
	}

}
