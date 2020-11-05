package com.sumavision.tetris.cs.channel.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ChannelStatusErrorException extends BaseException{
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
private static final Logger LOG = LoggerFactory.getLogger(ChannelStatusErrorException.class);
	
	public ChannelStatusErrorException(String status, String action) {
		super(StatusCode.CONFLICT, "当前状态冲突");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("当前状态为：")
				.append(status)
				.append(",与预执行操作")
				.append(action)
				.append("冲突")
				.toString());
	}
}
