package com.sumavision.tetris.user.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class DuplicateUserNumberImportedException extends BaseException{

	private static final long serialVersionUID = 1L;

	public DuplicateUserNumberImportedException(String userno) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("导入的用户号码重复：").append(userno).toString());
	}

}
