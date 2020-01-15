package com.sumavision.bvc.device.command.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class HasNotUsefulPlayerException extends BaseException{
	
	private static final long serialVersionUID = 1L;

	public HasNotUsefulPlayerException() {
		super(StatusCode.FORBIDDEN, "没有可用的播放器！");
	}

}
