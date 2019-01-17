package com.sumavision.tetris.easy.process.api.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class RepeatedVariableValueCheckFailedException extends BaseException{

	private static final Logger LOG = LoggerFactory.getLogger(RepeatedVariableValueCheckFailedException.class);
	
	private static final long serialVersionUID = 1L;

	public RepeatedVariableValueCheckFailedException(String primaryKey, String name, String value, String expression) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("接入点返回值与参数定义相同，但返回值用参数定义校验未通过！")
															 .append(primaryKey)
															 .append("(")
															 .append(name)
															 .append(")值: ")
															 .append(value)
															 .append("约束: ")
															 .append(expression)
															 .toString());
		LOG.error(new StringBufferWrapper().append("接入点返回值与参数定义相同，但返回值用参数定义校验未通过！")
										   .append(primaryKey)
										   .append("(")
										   .append(name)
										   .append(")值: ")
										   .append(value)
										   .append("约束: ")
										   .append(expression)
										   .toString());
	}

}
