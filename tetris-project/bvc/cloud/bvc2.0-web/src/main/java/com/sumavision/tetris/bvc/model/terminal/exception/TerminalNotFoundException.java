package com.sumavision.tetris.bvc.model.terminal.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class TerminalNotFoundException extends BaseException{

	private static final long serialVersionUID = 1L;

	public TerminalNotFoundException(Long id) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("终端不存在，id：").append(id).toString());
	}

}
