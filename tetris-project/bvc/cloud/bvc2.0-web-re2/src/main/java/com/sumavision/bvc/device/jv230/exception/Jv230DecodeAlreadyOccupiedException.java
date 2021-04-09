package com.sumavision.bvc.device.jv230.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class Jv230DecodeAlreadyOccupiedException extends BaseException{

	private static final long serialVersionUID = 1L;

	public Jv230DecodeAlreadyOccupiedException() {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("屏幕配置超过大屏解码能力")
															 .toString());
	}
}
