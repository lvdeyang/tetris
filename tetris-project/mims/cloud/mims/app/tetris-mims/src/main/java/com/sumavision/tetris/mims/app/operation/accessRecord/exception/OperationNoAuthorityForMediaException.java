package com.sumavision.tetris.mims.app.operation.accessRecord.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class OperationNoAuthorityForMediaException extends BaseException{
	private static final Logger LOG = LoggerFactory.getLogger(OperationNoAuthorityForMediaException.class);
	
	private static final long serialVersionUID = 1L;
	
	public OperationNoAuthorityForMediaException(String name) {
		super(StatusCode.FORBIDDEN, "无使用资源权限！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("您未拥有资源：")
											.append(name)
											.append("的使用权限！")
										   .toString());
	}
}
