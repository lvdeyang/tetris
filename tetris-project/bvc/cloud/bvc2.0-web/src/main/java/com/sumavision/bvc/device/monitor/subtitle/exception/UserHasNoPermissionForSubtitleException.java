package com.sumavision.bvc.device.monitor.subtitle.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class UserHasNoPermissionForSubtitleException extends BaseException{

	private static final long serialVersionUID = 1L;

	public UserHasNoPermissionForSubtitleException(Long subtitleId) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("您对当前字幕没有权限！字幕id：")
															 .append(subtitleId)
															 .toString());
	}

}
