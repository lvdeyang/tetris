package com.sumavision.tetris.mims.app.media.tag.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class UserHasNoGroupException extends BaseException{
	private final Logger LOG = LoggerFactory.getLogger(UserHasNoGroupException.class);

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserHasNoGroupException(String name) {
		super(StatusCode.FORBIDDEN, "当前用户非组织用户");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("用户")
				.append(name)
				.append("非组织用户")
				.toString());
	}
}
