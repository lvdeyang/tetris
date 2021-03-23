package com.sumavision.tetris.cs.schedule.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ProgramNotExistsException extends BaseException{
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(ProgramNotExistsException.class);
	
	public ProgramNotExistsException(String name) {
		super(StatusCode.NOTFOUND, "节目不存在或未添加资源"+name);
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("节目:")
				.append(name)
				.append("不存在")
				.toString());
	}

}
