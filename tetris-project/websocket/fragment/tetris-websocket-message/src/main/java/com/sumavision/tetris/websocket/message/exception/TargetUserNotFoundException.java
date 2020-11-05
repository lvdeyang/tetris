package com.sumavision.tetris.websocket.message.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;

public class TargetUserNotFoundException extends BaseException{

	private static final long serialVersionUID = 1L;
	
	private static final Logger LOG = LoggerFactory.getLogger(TargetUserNotFoundException.class);

	public TargetUserNotFoundException() {
		super(StatusCode.FORBIDDEN, "目标用户不存在！");
		LOG.error(DateUtil.now());
		LOG.error("消息推送，目标用户不存在！");
	}

}
