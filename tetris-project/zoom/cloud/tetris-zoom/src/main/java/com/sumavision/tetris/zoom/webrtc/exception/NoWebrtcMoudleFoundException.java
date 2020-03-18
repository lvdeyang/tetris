package com.sumavision.tetris.zoom.webrtc.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class NoWebrtcMoudleFoundException extends BaseException{

	private static final long serialVersionUID = 1L;

	public NoWebrtcMoudleFoundException() {
		super(StatusCode.FORBIDDEN, "系统中未获取到webrtc模块！");
	}
	
}
