package com.sumavision.tetris.bvc.model.role.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class RoleChannelNotFoundException extends BaseException{

	private static final long serialVersionUID = 1L;

	public RoleChannelNotFoundException(Long id) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("角色通道粗存在，id：")
															 .append(id)
															 .toString());
	}

}
