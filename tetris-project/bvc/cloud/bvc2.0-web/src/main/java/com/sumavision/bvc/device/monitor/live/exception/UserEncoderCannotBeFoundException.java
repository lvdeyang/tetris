package com.sumavision.bvc.device.monitor.live.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class UserEncoderCannotBeFoundException extends BaseException{

	private static final long serialVersionUID = 1L;

	public UserEncoderCannotBeFoundException() {
		super(StatusCode.FORBIDDEN, "当前用户未绑定编码器！");
	}

}
