package com.sumavision.bvc.device.monitor.point.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class UserHasNoPermissionForPointException extends BaseException{

	private static final long serialVersionUID = 1L;

	public UserHasNoPermissionForPointException() {
		super(StatusCode.FORBIDDEN, "您没有权限操作此预置点！");
	}

}
