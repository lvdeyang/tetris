package com.sumavision.tetris.easy.process.core.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ProcessInUseException extends BaseException{

	private static final Logger LOG = LoggerFactory.getLogger(ProcessInUseException.class);
	
	private static final long serialVersionUID = 1L;

	public ProcessInUseException(Long processId, String message) {
		super(StatusCode.FORBIDDEN, "流程正在使用中！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("流程正在使用中！流程id：")
										   .append(processId)
										   .append("，")
										   .append(message)
										   .toString());
	}

}
