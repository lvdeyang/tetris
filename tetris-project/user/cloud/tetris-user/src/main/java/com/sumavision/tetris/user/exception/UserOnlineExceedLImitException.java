package com.sumavision.tetris.user.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class UserOnlineExceedLImitException extends BaseException{
private static final Logger LOG = LoggerFactory.getLogger(UsernameNotExistException.class);
	
	private static final long serialVersionUID = 1L;

	public UserOnlineExceedLImitException() {
		super(StatusCode.FORBIDDEN, "当前在线用户达到上限！", "/web/app/login/login.html");
		LOG.error("当前在线用户达到上限 ！");
	}
	
	public UserOnlineExceedLImitException(String str) {
		super(StatusCode.FORBIDDEN, "当前在线用户达到上限！", "/web/app/login/login.html");
		LOG.error(new StringBufferWrapper().append("当前在线用户已达到")
										   .append(str)
										   .append("上限！")
										   .toString());
	}

}
