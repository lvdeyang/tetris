package com.sumavision.tetris.bvc.model.terminal.screen.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ScreenPrimaryKeyNotFoundException extends BaseException{

	private static final long serialVersionUID = 1L;

	public ScreenPrimaryKeyNotFoundException(Long id) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("屏幕主键不存在，id：")
															 .append(id)
															 .toString());
	}
	
	public ScreenPrimaryKeyNotFoundException(String screenPrimaryKey) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("屏幕主键不存在，主键：")
															 .append(screenPrimaryKey)
															 .toString());
	}

}
