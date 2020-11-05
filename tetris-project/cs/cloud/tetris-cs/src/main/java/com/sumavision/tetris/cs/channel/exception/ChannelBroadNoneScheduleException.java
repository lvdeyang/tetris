package com.sumavision.tetris.cs.channel.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ChannelBroadNoneScheduleException extends BaseException{
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(ChannelBroadNoneOutputException.class);
	
	public ChannelBroadNoneScheduleException() {
		super(StatusCode.FORBIDDEN, "频道无有效排期");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("频道无有效排期")
				.toString());
	}
}
