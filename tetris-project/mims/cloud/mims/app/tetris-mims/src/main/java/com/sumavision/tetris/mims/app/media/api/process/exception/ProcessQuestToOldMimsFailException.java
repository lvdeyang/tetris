package com.sumavision.tetris.mims.app.media.api.process.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ProcessQuestToOldMimsFailException extends BaseException{
	private final Logger LOG = LoggerFactory.getLogger(ProcessQuestToOldMimsFailException.class);
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ProcessQuestToOldMimsFailException(String action) {
		super(StatusCode.ERROR, "请求旧媒资失败！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("请求:")
										   .append(action)
										   .toString());
	}
}
