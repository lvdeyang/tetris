package com.sumavision.tetris.mims.app.media.compress.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mims.app.media.UploadStatus;

public class MediaCompressStatusErrorWhenUploadErrorException extends BaseException{

	private final Logger LOG = LoggerFactory.getLogger(MediaCompressStatusErrorWhenUploadErrorException.class);
	
	private static final long serialVersionUID = 1L;

	public MediaCompressStatusErrorWhenUploadErrorException(String uuid, UploadStatus status) {
		super(StatusCode.ERROR, "无法修改上传错误状态！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("任务:")
										   .append(uuid)
										   .append(", 状态:")
										   .append(status.toString())
										   .toString());
	}

}
