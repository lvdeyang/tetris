package com.sumavision.tetris.cs.channel.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ChannelNotExistsException extends BaseException{

	private static final Logger LOG = LoggerFactory.getLogger(ChannelNotExistsException.class);
	
	private static final long serialVersionUID = 1L;

	public ChannelNotExistsException(Long id) {
		super(StatusCode.FORBIDDEN, "频道不存在！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("手机号不存在！")
										   .append(id)
										   .toString());
	}

}
