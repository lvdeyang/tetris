package com.sumavision.tetris.mims.app.media.stream.audio.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class MediaAudioStreamNotExistException extends BaseException{

	private static final Logger LOG = LoggerFactory.getLogger(MediaAudioStreamNotExistException.class);
	
	private static final long serialVersionUID = 1L;

	public MediaAudioStreamNotExistException(String uuid) {
		super(StatusCode.FORBIDDEN, "音频流媒资不存在！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("音频流媒资不存在！uuid:")
										   .append(uuid)
										   .toString());
	}
	
	public MediaAudioStreamNotExistException(Long id) {
		super(StatusCode.FORBIDDEN, "音频流媒资不存在！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("音频流媒资不存在！id:")
										   .append(id)
										   .toString());
	}

}
