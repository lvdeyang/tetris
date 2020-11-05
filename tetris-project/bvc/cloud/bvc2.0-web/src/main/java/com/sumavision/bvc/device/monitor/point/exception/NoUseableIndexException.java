package com.sumavision.bvc.device.monitor.point.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class NoUseableIndexException extends BaseException{

	/** 这是一个常量的说明 */
	private static final long serialVersionUID = 1L;

	public NoUseableIndexException() {
		super(StatusCode.FORBIDDEN, "预置点已经达到最大数量！");
	}
	
}
