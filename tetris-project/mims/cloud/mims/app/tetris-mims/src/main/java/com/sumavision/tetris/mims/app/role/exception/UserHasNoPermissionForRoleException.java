package com.sumavision.tetris.mims.app.role.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mims.app.organization.exception.UserHasNoPermissionForOrganizationException;

public class UserHasNoPermissionForRoleException extends BaseException{

	private static final Logger LOG = LoggerFactory.getLogger(UserHasNoPermissionForOrganizationException.class);
	
	private static final long serialVersionUID = 1L;

	public UserHasNoPermissionForRoleException(String userId, Long roleId) {
		super(StatusCode.ERROR, "您没有权限！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("没有操作权限，用户id：")
										   .append(userId)
										   .append("，权限id：")
										   .append(roleId)
										   .toString());
	}

	public UserHasNoPermissionForRoleException(String userId){
		super(StatusCode.ERROR, "您没有权限！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("没有操作权限，用户id：")
										   .append(userId)
										   .toString());
	}
	
}
