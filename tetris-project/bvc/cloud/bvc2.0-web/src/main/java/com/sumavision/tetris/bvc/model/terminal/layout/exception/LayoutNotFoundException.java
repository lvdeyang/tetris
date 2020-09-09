package com.sumavision.tetris.bvc.model.terminal.layout.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class LayoutNotFoundException extends BaseException{

	private static final long serialVersionUID = 1L;

	public LayoutNotFoundException(Long id) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("布局不存在，id：")
															 .append(id)
															 .toString());
	}

	public LayoutNotFoundException(String name) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("布局不存在，name：")
															 .append(name)
															 .toString());
	}
	
}
