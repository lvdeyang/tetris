package com.sumavision.tetris.cs.channel.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ChannelUdpBroadCountIsFullException extends BaseException{
	private final Logger LOG = LoggerFactory.getLogger(ChannelUdpBroadCountIsFullException.class);
	
	private static final long serialVersionUID = 1L;

	public ChannelUdpBroadCountIsFullException() {
		super(StatusCode.CONFLICT, "轮播推流播发的频道已达数量上限");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("轮播推流播发的频道已达数量上限!")
										   .toString());
	}
}
