package com.sumavision.bvc.device.command.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class UserHasNoFolderException extends BaseException{

	private static final long serialVersionUID = 1L;

	public UserHasNoFolderException(String userName) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper()
				 .append(userName)
				 .append("用户没有分配组织机构")
				 .toString());
	}

}
