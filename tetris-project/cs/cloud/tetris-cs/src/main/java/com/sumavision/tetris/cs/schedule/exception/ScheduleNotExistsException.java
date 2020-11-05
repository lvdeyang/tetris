package com.sumavision.tetris.cs.schedule.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ScheduleNotExistsException extends BaseException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(ScheduleNotExistsException.class);
	
	public ScheduleNotExistsException(Long id) {
		super(StatusCode.NOTFOUND, "当前排期不存在");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("排期id:")
				.append(id)
				.append("不存在")
				.toString());
	}
}
