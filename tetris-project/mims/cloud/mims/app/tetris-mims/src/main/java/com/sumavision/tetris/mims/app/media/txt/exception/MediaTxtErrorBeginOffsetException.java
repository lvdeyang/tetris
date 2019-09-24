package com.sumavision.tetris.mims.app.media.txt.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class MediaTxtErrorBeginOffsetException extends BaseException{
	
	private final Logger LOG = LoggerFactory.getLogger(MediaTxtErrorBeginOffsetException.class);
	
	private static final long serialVersionUID = 1L;

	public MediaTxtErrorBeginOffsetException(String uuid, long beginOffset, long realBeginOffset) {
		super(StatusCode.FORBIDDEN, "文件起始位置错误，无法继续上传！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("任务:")
										   .append(uuid)
										   .append(", 参数:")
										   .append(beginOffset)
										   .append(", 文件:")
										   .append(realBeginOffset)
										   .toString());
	}

}
