package com.sumavision.tetris.cs.channel.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ChannelAlreadyBroadException extends BaseException{
	private final Logger LOG = LoggerFactory.getLogger(ChannelAlreadyBroadException.class);
	
	private static final long serialVersionUID = 1L;

	public ChannelAlreadyBroadException(String channelName) {
		super(StatusCode.CONFLICT, "频道当前为已为播发状态");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("状态冲突!")
				.append("频道： ")
				.append(channelName)
				.append("不可播发")
				.toString());
	}
}
