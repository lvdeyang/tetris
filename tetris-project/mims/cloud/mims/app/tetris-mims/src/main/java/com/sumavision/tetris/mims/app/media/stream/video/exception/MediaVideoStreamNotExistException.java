package com.sumavision.tetris.mims.app.media.stream.video.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class MediaVideoStreamNotExistException extends BaseException{

	private static final Logger LOG = LoggerFactory.getLogger(MediaVideoStreamNotExistException.class);
	
	private static final long serialVersionUID = 1L;

	public MediaVideoStreamNotExistException(String uuid) {
		super(StatusCode.FORBIDDEN, "视频流媒资不存在！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("视频流媒资不存在！uuid:")
										   .append(uuid)
										   .toString());
	}
	
	public MediaVideoStreamNotExistException(Long id) {
		super(StatusCode.FORBIDDEN, "视频流媒资不存在！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("视频流媒资不存在！id:")
										   .append(id)
										   .toString());
	}

}
