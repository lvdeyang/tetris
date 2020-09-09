package com.sumavision.tetris.easy.process.access.service.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ServiceNotExistException extends BaseException{

	private static final Logger LOG = LoggerFactory.getLogger(ServiceNotExistException.class);
	
	private static final long serialVersionUID = 1L;

	public ServiceNotExistException(Long id) {
		super(StatusCode.FORBIDDEN, "服务不存在！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("服务不存在，服务id：")
										   .append(id)
										   .toString());
	}
	
}
