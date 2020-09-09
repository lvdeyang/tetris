package com.sumavision.tetris.cs.channel.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ChannelUdpIpAndPortAlreadyExistException extends BaseException{
	private final Logger LOG = LoggerFactory.getLogger(ChannelUdpIpAndPortAlreadyExistException.class);
	
	private static final long serialVersionUID = 1L;

	public ChannelUdpIpAndPortAlreadyExistException(String ip, String port) {
		super(StatusCode.CONFLICT, "已存在相同流ip和流port频道");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("已存在相同流ip和流port频道!")
				.append("冲突的ip为：")
				.append(ip)
				.append("port为：")
				.append(port)
				.toString());
	}
	
}
