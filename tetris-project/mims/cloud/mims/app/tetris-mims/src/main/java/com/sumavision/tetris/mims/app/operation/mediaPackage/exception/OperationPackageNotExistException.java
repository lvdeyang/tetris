package com.sumavision.tetris.mims.app.operation.mediaPackage.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class OperationPackageNotExistException extends BaseException{
	private static final Logger LOG = LoggerFactory.getLogger(OperationPackageNotExistException.class);
	
	private static final long serialVersionUID = 1L;
	
	public OperationPackageNotExistException(Long id) {
		super(StatusCode.FORBIDDEN, "套餐不存在！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("套餐不存在！id:")
										   .append(id)
										   .toString());
	}
	
	public OperationPackageNotExistException() {
		super(StatusCode.FORBIDDEN, "套餐不存在！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("查询的套餐均不存在！")
										   .toString());
	}
}
