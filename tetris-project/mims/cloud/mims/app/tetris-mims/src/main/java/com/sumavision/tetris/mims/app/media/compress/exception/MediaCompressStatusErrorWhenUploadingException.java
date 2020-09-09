package com.sumavision.tetris.mims.app.media.compress.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mims.app.media.UploadStatus;

public class MediaCompressStatusErrorWhenUploadingException extends BaseException{

	private final Logger LOG = LoggerFactory.getLogger(MediaCompressStatusErrorWhenUploadingException.class);
	
	private static final long serialVersionUID = 1L;

	public MediaCompressStatusErrorWhenUploadingException(String uuid, UploadStatus status) {
		super(StatusCode.FORBIDDEN, "任务状态错误, 无法继续上传！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("任务:")
										   .append(uuid)
										   .append(", 状态:")
										   .append(status.toString())
										   .toString());
	}

}
