package com.sumavision.tetris.easy.process.access.point.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ConstraintNotExistException extends BaseException{

	private static final Logger LOG = LoggerFactory.getLogger(ConstraintNotExistException.class);
	
	private static final long serialVersionUID = 1L;

	public ConstraintNotExistException(Long id) {
		super(StatusCode.FORBIDDEN, "约束不存在！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("约束id：")
										   .append(id)
										   .toString());
	}

}
