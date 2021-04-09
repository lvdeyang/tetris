package com.sumavision.bvc.device.monitor.live.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class UserBusyException extends BaseException{

	private static final long serialVersionUID = 1L;

	public UserBusyException(Long userId) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("当前用户正忙！")
															 .append(userId)
															 .toString());
	}
	
	public UserBusyException(){
		super(StatusCode.FORBIDDEN, "当前用户正忙！");
	}

}
