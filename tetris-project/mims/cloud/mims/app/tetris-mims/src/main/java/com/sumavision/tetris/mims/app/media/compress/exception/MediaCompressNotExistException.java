package com.sumavision.tetris.mims.app.media.compress.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class MediaCompressNotExistException extends BaseException{

	private static final Logger LOG = LoggerFactory.getLogger(MediaCompressNotExistException.class);
	
	private static final long serialVersionUID = 1L;

	public MediaCompressNotExistException(String uuid) {
		super(StatusCode.FORBIDDEN, "图片媒资不存在！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("图片媒资不存在！uuid:")
										   .append(uuid)
										   .toString());
	}
	
	public MediaCompressNotExistException(Long id) {
		super(StatusCode.FORBIDDEN, "图片媒资不存在！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("图片媒资不存在！id:")
										   .append(id)
										   .toString());
	}

}
