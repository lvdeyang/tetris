package com.sumavision.tetris.mims.app.media.live.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class MediaPushLiveNotExistException extends BaseException{

	private static final Logger LOG = LoggerFactory.getLogger(MediaPushLiveNotExistException.class);
	
	private static final long serialVersionUID = 1L;

	public MediaPushLiveNotExistException(String uuid) {
		super(StatusCode.FORBIDDEN, "push直播媒资不存在！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("push直播媒资不存在！uuid:")
										   .append(uuid)
										   .toString());
	}
	
	public MediaPushLiveNotExistException(Long id) {
		super(StatusCode.FORBIDDEN, "push直播媒资不存在！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("push直播媒资不存在！id:")
										   .append(id)
										   .toString());
	}

}
