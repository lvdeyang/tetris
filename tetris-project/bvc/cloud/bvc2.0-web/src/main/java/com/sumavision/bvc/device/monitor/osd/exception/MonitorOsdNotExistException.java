package com.sumavision.bvc.device.monitor.osd.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class MonitorOsdNotExistException extends BaseException{

	private static final long serialVersionUID = 1L;

	public MonitorOsdNotExistException(Long id) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("osd不存在，id：")
															 .append(id)
															 .toString());
	}

}
