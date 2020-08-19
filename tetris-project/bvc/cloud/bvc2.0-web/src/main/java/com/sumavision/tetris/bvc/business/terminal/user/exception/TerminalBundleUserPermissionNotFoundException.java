package com.sumavision.tetris.bvc.business.terminal.user.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class TerminalBundleUserPermissionNotFoundException extends BaseException{

	private static final long serialVersionUID = 1L;

	public TerminalBundleUserPermissionNotFoundException(Long id) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("设备信息不存在，id：").append(id).toString());
	}

}
