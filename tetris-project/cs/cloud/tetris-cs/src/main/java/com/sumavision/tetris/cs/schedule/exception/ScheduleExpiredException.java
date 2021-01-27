/**
 * 
 */
package com.sumavision.tetris.cs.schedule.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

/**
 * 类型概述<br/>
 * <b>作者:</b>zhouaining<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年1月27日 上午10:40:06
 */
public class ScheduleExpiredException extends BaseException{
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(ScheduleNoneToBroadException.class);

	public ScheduleExpiredException(String name) {
		super(StatusCode.FORBIDDEN, "当前频道排期已过期");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("频道：")
				.append(name)
				.append("排期已过期")
				.toString());
	}
}
