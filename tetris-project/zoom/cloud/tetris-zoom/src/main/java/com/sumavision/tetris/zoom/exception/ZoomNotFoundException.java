package com.sumavision.tetris.zoom.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ZoomNotFoundException extends BaseException{

	private static final long serialVersionUID = 1L;

	public ZoomNotFoundException(String code) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("当前会议不存在！会议号码：").append(code).toString());
	}
	
	public ZoomNotFoundException(Long id) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("当前会议不存在！会议id：").append(id).toString());
	}

}
