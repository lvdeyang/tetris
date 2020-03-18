package com.sumavision.tetris.user.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class DeletedUserIsNotATouristException extends BaseException{

	private static final long serialVersionUID = 1L;

	public DeletedUserIsNotATouristException(Long userId) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("当前要清除的用户不是游客，userId：").append(userId).toString());
	}

}
