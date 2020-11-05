package com.sumavision.tetris.user.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class TokenUpdatedException extends BaseException{
	private static final Logger LOG = LoggerFactory.getLogger(TokenUpdatedException.class);

	private static final long serialVersionUID = 1L;

	public TokenUpdatedException() {
		super(StatusCode.TIMEOUT, "签名更新，请重新登录！");
		LOG.error(new StringBufferWrapper().append("签名更新，请重新登录！")
										   .toString());
	}
}
