package com.sumavision.tetris.mims.app.operation.statistic.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class OperationStatisticStrategyNotExistException extends BaseException{
private static final Logger LOG = LoggerFactory.getLogger(OperationStatisticStrategyNotExistException.class);
	
	private static final long serialVersionUID = 1L;
	
	public OperationStatisticStrategyNotExistException(Long id) {
		super(StatusCode.FORBIDDEN, "结算策略不存在！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("结算策略不存在！id:")
										   .append(id)
										   .toString());
	}
}
