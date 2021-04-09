package com.sumavision.bvc.device.monitor.osd.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class MonitorOsdLayerNotExistException extends BaseException{

	private static final long serialVersionUID = 1L;

	public MonitorOsdLayerNotExistException(Long layerId) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("图层不存在！id：")
															 .append(layerId)
															 .toString());
	}

}
