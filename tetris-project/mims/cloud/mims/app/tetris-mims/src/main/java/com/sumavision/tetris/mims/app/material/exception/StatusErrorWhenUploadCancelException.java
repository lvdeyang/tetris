package com.sumavision.tetris.mims.app.material.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mims.app.material.MaterialFileUploadStatus;

public class StatusErrorWhenUploadCancelException extends BaseException{

	private final Logger LOG = LoggerFactory.getLogger(StatusErrorWhenUploadCancelException.class);
	
	private static final long serialVersionUID = 1L;
	
	public StatusErrorWhenUploadCancelException(String uuid, MaterialFileUploadStatus status) {
		super(StatusCode.FORBIDDEN, "当前文件已经上传完成，关闭失败！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("任务: ")
										   .append(uuid)
										   .append(", 状态:")
										   .append(status.toString())
										   .toString());
	}
	
}
