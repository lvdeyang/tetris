package com.sumavision.tetris.mims.app.media.picture.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class UserHashNoPermissionForMediaPictureException extends BaseException{

	private static final Logger LOG = LoggerFactory.getLogger(UserHashNoPermissionForMediaPictureException.class);
	
	private static final long serialVersionUID = 1L;

	public UserHashNoPermissionForMediaPictureException(String userId) {
		super(StatusCode.ERROR, "您没有权限！");
		LOG.error(new StringBufferWrapper().append("用户id：")
										   .append(userId)
										   .toString());
	}
	
}
