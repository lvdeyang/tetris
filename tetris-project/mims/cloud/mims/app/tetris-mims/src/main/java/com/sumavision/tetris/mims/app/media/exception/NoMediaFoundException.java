package com.sumavision.tetris.mims.app.media.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class NoMediaFoundException extends BaseException{
	
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(NoMediaFoundException.class);
	 
	public NoMediaFoundException(String name) {
		super(StatusCode.NOTFOUND, "未找到对应媒资:"+name);
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("媒资:")
				.append(name)
				.append("不存在")
				.toString());
	}

}
