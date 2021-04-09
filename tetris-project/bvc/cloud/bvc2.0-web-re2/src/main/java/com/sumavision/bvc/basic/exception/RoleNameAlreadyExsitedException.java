package com.sumavision.bvc.basic.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class RoleNameAlreadyExsitedException extends BaseException{

	private static final long serialVersionUID = 1L;

	public RoleNameAlreadyExsitedException(String name) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("该角色名已存在，角色名为：")
															 .append(name)
															 .toString());
	}

}
