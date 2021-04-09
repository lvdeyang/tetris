package com.sumavision.bvc.device.monitor.playback.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class AccessNodeNotExistException extends BaseException{

	private static final long serialVersionUID = 1L;

	public AccessNodeNotExistException(String layerId) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("未获取到接入层：")
															 .append(layerId)
															 .toString());
	}

}
