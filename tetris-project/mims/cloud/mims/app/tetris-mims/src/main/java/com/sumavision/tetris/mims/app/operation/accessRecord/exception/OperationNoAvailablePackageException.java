package com.sumavision.tetris.mims.app.operation.accessRecord.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class OperationNoAvailablePackageException extends BaseException{
private static final Logger LOG = LoggerFactory.getLogger(OperationNoAvailablePackageException.class);
	
	private static final long serialVersionUID = 1L;
	
	public OperationNoAvailablePackageException(Long userId) {
		super(StatusCode.FORBIDDEN, "用户无可使用套餐！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("用户id：")
											.append(userId)
											.append("，无可使用套餐！")
										   .toString());
	}
	
	public OperationNoAvailablePackageException(String name) {
		super(StatusCode.FORBIDDEN, "用户无可使用套餐！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("用户：")
											.append(name)
											.append("，无可使用套餐！")
										   .toString());
	}
}
