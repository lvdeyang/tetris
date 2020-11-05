package com.sumavision.bvc.device.monitor.subtitle.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class SubtitleNotExistException extends BaseException{

	private static final long serialVersionUID = 1L;

	public SubtitleNotExistException(Long subtitleId) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("字幕不存在！字幕id：")
															 .append(subtitleId)
															 .toString());
	}

}
