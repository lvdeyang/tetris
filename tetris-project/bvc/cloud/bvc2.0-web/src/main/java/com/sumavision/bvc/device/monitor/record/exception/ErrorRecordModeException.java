package com.sumavision.bvc.device.monitor.record.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ErrorRecordModeException extends BaseException{

	private static final long serialVersionUID = 1L;

	public ErrorRecordModeException(String mode) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("错误的录制模式！")
															 .append(mode)
															 .toString());
	}

}
