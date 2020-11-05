package com.sumavision.bvc.device.monitor.point.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class MonitorPointNotExistException extends BaseException{

	private static final long serialVersionUID = 1L;

	public MonitorPointNotExistException() {
		super(StatusCode.FORBIDDEN, "预支点不存在！");
	}

}
