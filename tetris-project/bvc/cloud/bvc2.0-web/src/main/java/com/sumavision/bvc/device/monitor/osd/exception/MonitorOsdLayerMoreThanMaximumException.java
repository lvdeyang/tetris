package com.sumavision.bvc.device.monitor.osd.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class MonitorOsdLayerMoreThanMaximumException extends BaseException{

	private static final long serialVersionUID = 1L;

	public MonitorOsdLayerMoreThanMaximumException(int maximum) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("osd图层超过最大数量：")
															 .append(maximum)
															 .toString());
	}
	
}
