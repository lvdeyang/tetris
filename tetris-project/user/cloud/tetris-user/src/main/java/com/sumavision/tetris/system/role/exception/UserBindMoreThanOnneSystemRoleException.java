package com.sumavision.tetris.system.role.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class UserBindMoreThanOnneSystemRoleException extends BaseException{

	private static final long serialVersionUID = 1L;

	public UserBindMoreThanOnneSystemRoleException() {
		super(StatusCode.FORBIDDEN, "用户只能绑定一个系统角色！");
	}

}
