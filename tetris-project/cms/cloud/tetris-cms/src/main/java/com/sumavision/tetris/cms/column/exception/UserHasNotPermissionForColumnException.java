package com.sumavision.tetris.cms.column.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.user.UserVO;

public class UserHasNotPermissionForColumnException extends BaseException{
	
	private static final Logger LOG = LoggerFactory.getLogger(UserHasNotPermissionForColumnException.class);
	
	private static final long serialVersionUID = 1L;

	public UserHasNotPermissionForColumnException(Long columnId, UserVO user) {
		super(StatusCode.ERROR, "您没有当前栏目的权限！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("栏目与用户不匹配!地区id：")
				   						   .append(columnId)
				   						   .append("用户组织id：")
				   						   .append(user.getGroupId())
				   						   .append("用户id：")
				   						   .append(user.getUuid())
				   						   .toString());
	}

}
