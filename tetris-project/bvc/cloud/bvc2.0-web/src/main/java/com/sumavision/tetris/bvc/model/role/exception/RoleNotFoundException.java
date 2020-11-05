package com.sumavision.tetris.bvc.model.role.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class RoleNotFoundException extends BaseException{

	private static final long serialVersionUID = 1L;

	public RoleNotFoundException(Long id) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("角色不存在，id：")
															 .append(id)
															 .toString());
	}

}
