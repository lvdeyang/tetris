package com.sumavision.tetris.cs.channel.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ChannelAbilityRequestErrorException extends BaseException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(ChannelAbilityRequestErrorException.class);
	
	public ChannelAbilityRequestErrorException(String type) {
		super(StatusCode.ERROR, "请求能力失败");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("能力请求：")
				.append(type)
				.append("失败")
				.toString());
	}
}
