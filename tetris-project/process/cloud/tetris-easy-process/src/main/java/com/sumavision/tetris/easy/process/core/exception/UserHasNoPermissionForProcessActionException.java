package com.sumavision.tetris.easy.process.core.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class UserHasNoPermissionForProcessActionException extends BaseException{

	private static final Logger LOG = LoggerFactory.getLogger(UserHasNoPermissionForProcessActionException.class); 
	
	private static final long serialVersionUID = 1L;

	public UserHasNoPermissionForProcessActionException(String userId, long id, String action) {
		super(StatusCode.FORBIDDEN, "您没有权限！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("用户：")
										   .append(userId)
										   .append("对流程：")
										   .append(id)
										   .append("没有权限！")
										   .append("流程操作：")
										   .append(action)
										   .toString());
	}

}
