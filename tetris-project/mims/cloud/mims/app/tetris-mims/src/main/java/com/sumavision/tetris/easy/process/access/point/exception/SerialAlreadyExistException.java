package com.sumavision.tetris.easy.process.access.point.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class SerialAlreadyExistException extends BaseException{

	private static final Logger LOG = LoggerFactory.getLogger(SerialAlreadyExistException.class);
	
	private static final long serialVersionUID = 1L;

	public SerialAlreadyExistException(Long accessPointId, Integer serial) {
		super(StatusCode.FORBIDDEN, "参数序号重复！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("参数序号重复！接入点id：")
										   .append(accessPointId)
										   .append(", 序号：")
										   .append(serial)
										   .toString());
	}

}
