package com.sumavision.tetris.zoom.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class ZoomStopedException extends BaseException{

	private static final long serialVersionUID = 1L;

	public ZoomStopedException() {
		super(StatusCode.FORBIDDEN, "当前会议已经停止");
	}

}
