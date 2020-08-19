package com.sumavision.tetris.cs.channel.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ChannelTerminalRequestErrorException extends BaseException{
	private final Logger LOG = LoggerFactory.getLogger(ChannelTerminalRequestErrorException.class);
	
	private static final long serialVersionUID = 1L;

	public ChannelTerminalRequestErrorException(String action, String msg) {
		super(StatusCode.FORBIDDEN, "请求终端播发失败");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("请求终端播发失败!")
				.append("播发动作")
				.append(action)
				.append(";错误信息：")
				.append(msg)
				.toString());
	}
}
