package com.sumavision.bvc.device.monitor.subtitle.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class SubtitleIsInUseException extends BaseException{

	private static final long serialVersionUID = 1L;

	public SubtitleIsInUseException(long layerId) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("当前字幕被使用，不能删除！占用图层id：")
														     .append(layerId)
														     .toString());
	}

}
