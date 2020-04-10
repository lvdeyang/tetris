package com.sumavision.tetris.user.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class UserCannotDeleteBecauseOnlineStatusException extends BaseException{

	private static final long serialVersionUID = 1L;

	public UserCannotDeleteBecauseOnlineStatusException() {
		super(StatusCode.FORBIDDEN, "用户在线，不能删除！");
	}

}
