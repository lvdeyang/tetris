package com.sumavision.tetris.cs.channel.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ChannelUdpUserIdAlreadyExistException extends BaseException{
	private final Logger LOG = LoggerFactory.getLogger(ChannelUdpUserIdAlreadyExistException.class);
	
	private static final long serialVersionUID = 1L;

	public ChannelUdpUserIdAlreadyExistException(String nickname, String channelName) {
		super(StatusCode.CONFLICT, "用户已被占用");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("预播发用户已被占用!")
				.append("该用户昵称为：")
				.append(nickname)
				.append("频道为：")
				.append(channelName)
				.toString());
	}
}
