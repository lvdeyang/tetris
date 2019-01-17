package com.sumavision.tetris.easy.process.api.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class VariableValueMissingException extends BaseException{

	private static final Logger LOG = LoggerFactory.getLogger(VariableValueMissingException.class);
	
	private static final long serialVersionUID = 1L;

	public VariableValueMissingException(String processPrimaryKey, String primaryKey, String name) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("流程：")
														     .append(processPrimaryKey)
														     .append("缺失变量：")
														     .append(primaryKey)
														     .append("(")
														     .append(name)
														     .append(")")
														     .append("的值")
														     .toString());
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("流程：")
									       .append(processPrimaryKey)
									       .append("缺失变量：")
									       .append(primaryKey)
									       .append("(")
									       .append(name)
									       .append(")")
									       .append("的值")
									       .toString());
	}

}
