package com.sumavision.tetris.data.warehouse.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class DataWarehouseRequestErrorException extends BaseException{
	private static final Logger LOG = LoggerFactory.getLogger(DataWarehouseRequestErrorException.class);
	
	private static final long serialVersionUID = 1L;

	public DataWarehouseRequestErrorException(String action, String err) {
		super(StatusCode.ERROR, "请求数据仓库失败！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("请求数据仓库失败！")
											.append("请求动作：")
											.append(action)
											.append(";错误信息：")
											.append(err)
											.toString());
	}
}
