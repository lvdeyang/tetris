package com.sumavision.tetris.mims.app.material.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class OffsetCannotMatchSizeException extends BaseException{

	private final Logger LOG = LoggerFactory.getLogger(OffsetCannotMatchSizeException.class);
	
	private static final long serialVersionUID = 1L;

	public OffsetCannotMatchSizeException(long beginOffset, long endOffset, long blockSize) {
		super(StatusCode.FORBIDDEN, "参数不匹配。");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("beginOffset:")
										   .append(beginOffset)
										   .append(", endOffset:")
										   .append(endOffset)
										   .append("blockSize:")
										   .append(blockSize)
										   .append("参数不匹配。")
										   .toString());
	}
	
}
