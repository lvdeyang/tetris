package com.sumavision.tetris.zoom.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class SourceGroupNotFoundException extends BaseException{

	private static final long serialVersionUID = 1L;

	public SourceGroupNotFoundException(Long id) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("分组不存在！id：").append(id).toString());
	}
	
}
