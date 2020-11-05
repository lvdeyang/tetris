package com.sumavision.tetris.streamTranscoding.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class MediaStreamHasNoPreviewUrlException extends BaseException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(MediaStreamHasNoPreviewUrlException.class);
	
	public MediaStreamHasNoPreviewUrlException(Long id) {
		super(StatusCode.ERROR, "流媒资无地址");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("媒资：")
				.append(id)
				.append("，无流地址")
				.toString());
	}
}
