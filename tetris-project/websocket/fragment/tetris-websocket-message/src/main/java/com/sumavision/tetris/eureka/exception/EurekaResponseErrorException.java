package com.sumavision.tetris.eureka.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class EurekaResponseErrorException extends BaseException{

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOG = LoggerFactory.getLogger(EurekaResponseErrorException.class);

	public EurekaResponseErrorException(String message) {
		super(StatusCode.FORBIDDEN, message);
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("eureka请求失败：").append(message).toString());
	}
	
}
