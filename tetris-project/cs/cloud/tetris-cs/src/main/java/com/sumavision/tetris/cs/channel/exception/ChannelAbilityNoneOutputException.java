package com.sumavision.tetris.cs.channel.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ChannelAbilityNoneOutputException extends BaseException{
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(ChannelAbilityNoneOutputException.class);
	
	public ChannelAbilityNoneOutputException() {
		super(StatusCode.ERROR, "无输出设置");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("无输出设置")
				.toString());
	}
}
