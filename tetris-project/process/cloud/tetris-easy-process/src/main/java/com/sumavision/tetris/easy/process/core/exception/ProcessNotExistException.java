package com.sumavision.tetris.easy.process.core.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ProcessNotExistException extends BaseException{

	private static final Logger LOG = LoggerFactory.getLogger(ProcessNotExistException.class);
	
	private static final long serialVersionUID = 1L;

	public ProcessNotExistException(Long id){
		super(StatusCode.FORBIDDEN, "流程不存在！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("流程不存在！流程id：")
										   .append(id)
										   .toString());
	}
	
	public ProcessNotExistException(String primaryKey){
		super(StatusCode.FORBIDDEN, "流程不存在！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("流程不存在！流程id：")
										   .append(primaryKey)
										   .toString());
	}
	
}
