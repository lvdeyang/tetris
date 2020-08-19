package com.sumavision.tetris.mims.app.media.txt.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class MediaTxtNotExistException extends BaseException{

	private static final Logger LOG = LoggerFactory.getLogger(MediaTxtNotExistException.class);
	
	private static final long serialVersionUID = 1L;

	public MediaTxtNotExistException(String uuid) {
		super(StatusCode.FORBIDDEN, "文本媒资不存在！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("文本媒资不存在！uuid:")
										   .append(uuid)
										   .toString());
	}
	
	public MediaTxtNotExistException(Long id) {
		super(StatusCode.FORBIDDEN, "文本媒资不存在！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("文本媒资不存在！id:")
										   .append(id)
										   .toString());
	}

}
