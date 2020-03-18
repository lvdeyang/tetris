package com.sumavision.tetris.zoom.webrtc.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class WebrtcResponseErrorException extends BaseException{

	private static final long serialVersionUID = 1L;

	public WebrtcResponseErrorException(String url, String message) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append(message).append("，webrtc模块:").append(url).toString());
	}

}
