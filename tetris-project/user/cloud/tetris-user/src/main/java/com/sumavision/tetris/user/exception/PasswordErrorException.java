package com.sumavision.tetris.user.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class PasswordErrorException extends BaseException{

	private static final Logger LOG = LoggerFactory.getLogger(PasswordErrorException.class);
	
	private static final long serialVersionUID = 1L;

	public PasswordErrorException() {
		super(StatusCode.FORBIDDEN, "密码输入错误！");
		LOG.error("密码输入错误！");
	}

}
