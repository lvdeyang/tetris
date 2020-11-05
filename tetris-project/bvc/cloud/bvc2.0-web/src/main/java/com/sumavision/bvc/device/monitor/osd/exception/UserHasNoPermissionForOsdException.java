package com.sumavision.bvc.device.monitor.osd.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class UserHasNoPermissionForOsdException extends BaseException{

	private static final long serialVersionUID = 1L;

	public UserHasNoPermissionForOsdException(Long osdId) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("您没有权限！osd：")
															 .append(osdId)
															 .toString());
	}

}
