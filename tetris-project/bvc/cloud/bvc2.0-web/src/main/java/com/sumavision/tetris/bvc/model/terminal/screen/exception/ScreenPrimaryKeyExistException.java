package com.sumavision.tetris.bvc.model.terminal.screen.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ScreenPrimaryKeyExistException extends BaseException{

	private static final long serialVersionUID = 1L;

	public ScreenPrimaryKeyExistException(String primaryKey) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("屏幕主键重复，primaryKey：")
															 .append(primaryKey)
															 .toString());
	}
	
	public ScreenPrimaryKeyExistException() {
		super(StatusCode.FORBIDDEN, "屏幕主键重复");
	}

}
