package com.sumavision.tetris.cs.schedule.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ScheduleNoneToAddException extends BaseException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(ScheduleNoneToAddException.class);
	
	public ScheduleNoneToAddException(Long id) {
		super(StatusCode.ERROR, "预添加排期为空");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("预为频道：")
				.append(id)
				.append("添加排期为空")
				.toString());
	}
}
