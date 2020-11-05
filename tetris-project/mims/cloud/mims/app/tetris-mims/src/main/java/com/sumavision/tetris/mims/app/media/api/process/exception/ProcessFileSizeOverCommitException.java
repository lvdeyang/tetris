package com.sumavision.tetris.mims.app.media.api.process.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mims.app.media.audio.exception.MediaAudioCannotMatchException;

public class ProcessFileSizeOverCommitException extends BaseException{
	private final Logger LOG = LoggerFactory.getLogger(ProcessFileSizeOverCommitException.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProcessFileSizeOverCommitException(String name, Long size) {
		super(StatusCode.ERROR, "上传文件大小不在允许范围内！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("文件名:")
										   .append(name)
										   .append(", size=")
										   .append(size)
										   .toString());
	}
}
