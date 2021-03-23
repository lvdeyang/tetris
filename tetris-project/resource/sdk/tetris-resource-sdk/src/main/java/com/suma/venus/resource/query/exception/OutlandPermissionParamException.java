package com.suma.venus.resource.query.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class OutlandPermissionParamException extends BaseException{

	private static final long serialVersionUID = 1L;

	public OutlandPermissionParamException(String message) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("连网推送外域的外域信息参数异常，")
															 .append(message)
															 .toString());
	}

}
