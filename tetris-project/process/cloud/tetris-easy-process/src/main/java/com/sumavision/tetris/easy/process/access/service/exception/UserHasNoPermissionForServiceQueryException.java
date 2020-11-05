package com.sumavision.tetris.easy.process.access.service.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class UserHasNoPermissionForServiceQueryException extends BaseException{

	private static final Logger LOG = LoggerFactory.getLogger(UserHasNoPermissionForServiceQueryException.class);
	
	private static final long serialVersionUID = 1L;

	public UserHasNoPermissionForServiceQueryException(String userId, String message) {
		super(StatusCode.ERROR, "您没有权限");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("用户id：")
										   .append(userId)
										   .append(", 操作：")
										   .append(message)
										   .toString());
	}
	
}
