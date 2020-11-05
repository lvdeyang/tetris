package com.sumavision.tetris.easy.process.access.point.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class PrimaryKeyAlreadyExistException extends BaseException{

	private static final Logger LOG = LoggerFactory.getLogger(PrimaryKeyAlreadyExistException.class);
	
	private static final long serialVersionUID = 1L;

	public PrimaryKeyAlreadyExistException(String primaryKey) {
		super(StatusCode.FORBIDDEN, "重复的参数主键！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("重复的参数主键:")
										   .append(primaryKey)
										   .toString());
	}

	
	
}
