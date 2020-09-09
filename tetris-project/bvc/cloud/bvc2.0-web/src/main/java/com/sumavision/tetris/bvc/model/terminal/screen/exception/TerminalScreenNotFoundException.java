package com.sumavision.tetris.bvc.model.terminal.screen.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class TerminalScreenNotFoundException extends BaseException{

	private static final long serialVersionUID = 1L;

	public TerminalScreenNotFoundException(Long id) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("屏幕不存在，id：")
															 .append(id)
															 .toString());
	}

}
