package com.sumavision.tetris.mims.app.material.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;

public class UserHasNoPermissionForMaterialException extends BaseException{

	private final Logger LOG = LoggerFactory.getLogger(UserHasNoPermissionForMaterialException.class);
	
	private static final long serialVersionUID = 1L;

	public UserHasNoPermissionForMaterialException(String userId, Long folderId) {
		super(StatusCode.ERROR, "您没有当前素材的权限！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBuffer().append("用户:")
									.append(userId)
									.append(", 文件夹:")
									.append(folderId)
									.append("; 权限不匹配！")
									.toString());
	}
	
}
