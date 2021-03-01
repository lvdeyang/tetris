package com.sumavision.tetris.capacity.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class HttpTimeoutException extends BaseException{
	
	private static final long serialVersionUID = 1L;

	public HttpTimeoutException(String ip) {
		super(StatusCode.ERROR, new StringBufferWrapper().append("转换模块请求超时，地址为：")
														 .append(ip)
														 .toString());
	}

}
