package com.sumavision.tetris.bvc.model.terminal.channel.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class TerminalChannelNotFoundException extends BaseException{

	private static final long serialVersionUID = 1L;

	public TerminalChannelNotFoundException(Long id) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("终端通道不存在，id：")
															 .append(id)
															 .toString());
	}

}
