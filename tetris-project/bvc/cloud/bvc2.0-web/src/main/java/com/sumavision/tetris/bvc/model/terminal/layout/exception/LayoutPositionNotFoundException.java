package com.sumavision.tetris.bvc.model.terminal.layout.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class LayoutPositionNotFoundException extends BaseException{

	private static final long serialVersionUID = 1L;

	public LayoutPositionNotFoundException(Long id) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("排版不存在，id：")
															 .append(id)
															 .toString());
	}

}
