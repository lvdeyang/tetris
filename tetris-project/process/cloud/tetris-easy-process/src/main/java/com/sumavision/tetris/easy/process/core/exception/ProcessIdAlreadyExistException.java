package com.sumavision.tetris.easy.process.core.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ProcessIdAlreadyExistException extends BaseException{

	private static final Logger LOG = LoggerFactory.getLogger(ProcessIdAlreadyExistException.class);
	
	private static final long serialVersionUID = 1L;

	public ProcessIdAlreadyExistException(String processId) {
		super(StatusCode.FORBIDDEN, "当前流程id在系统中已经存在！id拼写规则建议：公司首字母_功能_日期");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("流程id重复：")
										   .append(processId)
										   .toString());
	}

}
