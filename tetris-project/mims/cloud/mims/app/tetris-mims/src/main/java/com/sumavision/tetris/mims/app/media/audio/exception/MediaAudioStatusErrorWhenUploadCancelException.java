package com.sumavision.tetris.mims.app.media.audio.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mims.app.media.UploadStatus;

public class MediaAudioStatusErrorWhenUploadCancelException extends BaseException{

	private final Logger LOG = LoggerFactory.getLogger(MediaAudioStatusErrorWhenUploadCancelException.class);
	
	private static final long serialVersionUID = 1L;
	
	public MediaAudioStatusErrorWhenUploadCancelException(String uuid, UploadStatus status) {
		super(StatusCode.FORBIDDEN, "当前文件已经上传完成，关闭失败！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("任务: ")
										   .append(uuid)
										   .append(", 状态:")
										   .append(status.toString())
										   .toString());
	}
	
}
