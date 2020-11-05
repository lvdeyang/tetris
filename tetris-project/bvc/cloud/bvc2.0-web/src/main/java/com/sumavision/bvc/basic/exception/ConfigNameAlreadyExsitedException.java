package com.sumavision.bvc.basic.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ConfigNameAlreadyExsitedException extends BaseException{

	private static final long serialVersionUID = 1L;

	public ConfigNameAlreadyExsitedException(String name) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("议程名已存在，议程名为：")
															 .append(name)
															 .toString());
	}

}
