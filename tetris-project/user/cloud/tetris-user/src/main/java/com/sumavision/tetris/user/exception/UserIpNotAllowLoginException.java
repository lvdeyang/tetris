package com.sumavision.tetris.user.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class UserIpNotAllowLoginException extends BaseException{

	private static final Logger LOG = LoggerFactory.getLogger(UsernameNotExistException.class);
	
	private static final long serialVersionUID = 1L;

	public UserIpNotAllowLoginException(String loginIp) {
		super(StatusCode.FORBIDDEN, "ip地址不允许！", "/web/app/login/login.html");
		LOG.error(new StringBufferWrapper().append("当前ip ： ")
										   .append(loginIp)
										   .append(" 无法登陆该用户")
										   .toString());
	}

}


