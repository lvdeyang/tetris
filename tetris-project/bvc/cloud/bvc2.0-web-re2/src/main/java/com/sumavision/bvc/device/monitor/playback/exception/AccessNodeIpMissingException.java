package com.sumavision.bvc.device.monitor.playback.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class AccessNodeIpMissingException extends BaseException{

	private static final long serialVersionUID = 1L;

	public AccessNodeIpMissingException(String layerId) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("接入层ip缺失：")
															 .append(layerId)
															 .toString());
	}

}
