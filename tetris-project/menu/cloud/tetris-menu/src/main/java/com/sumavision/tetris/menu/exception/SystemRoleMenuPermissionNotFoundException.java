package com.sumavision.tetris.menu.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class SystemRoleMenuPermissionNotFoundException extends BaseException{

	private static final long serialVersionUID = 1L;

	public SystemRoleMenuPermissionNotFoundException(Long id){
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("未找到当前权限，id：").append(id).toString());
	}

}
