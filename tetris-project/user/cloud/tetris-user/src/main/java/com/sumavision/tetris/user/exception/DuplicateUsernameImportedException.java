package com.sumavision.tetris.user.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class DuplicateUsernameImportedException extends BaseException{

	private static final long serialVersionUID = 1L;

	public DuplicateUsernameImportedException(String username) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("导入的用户名重复：").append(username).toString());
	}

}
