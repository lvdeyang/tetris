package com.sumavision.tetris.test.flow.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ErrorReportStatusException extends BaseException{

	private static final long serialVersionUID = 1L;

	public ErrorReportStatusException(String name) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("错误的报告状态：")
															 .append(name)
															 .toString());
	}
	
}
