package com.sumavision.tetris.bvc.model.terminal.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class TerminalBundleNotFoundException extends BaseException{

	private static final long serialVersionUID = 1L;

	public TerminalBundleNotFoundException(Long terminalBundleId) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("终端设备不存在，id：")
															 .append(terminalBundleId)
															 .toString());
	}

}
