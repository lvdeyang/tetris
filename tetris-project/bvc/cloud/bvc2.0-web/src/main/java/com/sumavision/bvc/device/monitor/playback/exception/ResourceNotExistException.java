package com.sumavision.bvc.device.monitor.playback.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ResourceNotExistException extends BaseException{

	private static final long serialVersionUID = 1L;

	public ResourceNotExistException(String resourceId) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("资源不存在，或没有权限！资源id：")
															 .append(resourceId)
															 .toString());
	}

}
