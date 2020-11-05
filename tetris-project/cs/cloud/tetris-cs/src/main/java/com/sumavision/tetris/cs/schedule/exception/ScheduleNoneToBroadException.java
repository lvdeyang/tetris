package com.sumavision.tetris.cs.schedule.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ScheduleNoneToBroadException extends BaseException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(ScheduleNoneToBroadException.class);
	
	public ScheduleNoneToBroadException(String name) {
		super(StatusCode.ERROR, "当前频道无排期或无分屏设置");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("频道：")
				.append(name)
				.append("无排期或无分屏设置")
				.toString());
	}
}
