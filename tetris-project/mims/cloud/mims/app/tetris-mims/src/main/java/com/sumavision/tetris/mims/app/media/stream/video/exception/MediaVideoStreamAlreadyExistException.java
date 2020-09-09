package com.sumavision.tetris.mims.app.media.stream.video.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class MediaVideoStreamAlreadyExistException extends BaseException {
private static final Logger LOG = LoggerFactory.getLogger(MediaVideoStreamAlreadyExistException.class);
	
	private static final long serialVersionUID = 1L;
	
	public MediaVideoStreamAlreadyExistException(String url) {
		super(StatusCode.CONFLICT, "源地址已存在！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("源地址已存在！url:")
										   .append(url)
										   .toString());
	}
}
