package com.sumavision.tetris.cs.channel.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ChannelTerminalNoneAreaException extends BaseException{
	private final Logger LOG = LoggerFactory.getLogger(ChannelTerminalNoneAreaException.class);
	
	private static final long serialVersionUID = 1L;

	public ChannelTerminalNoneAreaException() {
		super(StatusCode.NOTFOUND, "播发地区为空，播发任务自动取消");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("播发地区为空!")
				.toString());
	}
}
