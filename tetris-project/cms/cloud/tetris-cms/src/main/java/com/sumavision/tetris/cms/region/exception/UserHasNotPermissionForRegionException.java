package com.sumavision.tetris.cms.region.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.user.UserVO;

public class UserHasNotPermissionForRegionException extends BaseException{
	
	private static final Logger LOG = LoggerFactory.getLogger(UserHasNotPermissionForRegionException.class);
	
	private static final long serialVersionUID = 1L;

	public UserHasNotPermissionForRegionException(Long regionId, UserVO user) {
		super(StatusCode.ERROR, "您没有当前地区的权限!");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("地区与用户不匹配!地区id：")
										   .append(regionId)
										   .append("用户组织id：")
										   .append(user.getGroupId())
										   .append("用户id：")
										   .append(user.getUuid())
										   .toString());
	}

}
