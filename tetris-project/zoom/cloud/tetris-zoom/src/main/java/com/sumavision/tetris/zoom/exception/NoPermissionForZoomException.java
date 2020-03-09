package com.sumavision.tetris.zoom.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class NoPermissionForZoomException extends BaseException{

	private static final long serialVersionUID = 1L;

	public NoPermissionForZoomException() {
		super(StatusCode.FORBIDDEN, "你没有当前会议的权限！");
	}
	
}
