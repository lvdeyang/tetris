package com.sumavision.tetris.cs.schedule.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ScheduleChannelIdNotSamException extends BaseException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(ScheduleChannelIdNotSamException.class);
	
	public ScheduleChannelIdNotSamException(Long id) {
		super(StatusCode.ERROR, "请求的排期列表不属于同一下发节目单");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("排期id：")
				.append(id)
				.append("与前排期表不属于同一下发节目单")
				.toString());
	}
}
