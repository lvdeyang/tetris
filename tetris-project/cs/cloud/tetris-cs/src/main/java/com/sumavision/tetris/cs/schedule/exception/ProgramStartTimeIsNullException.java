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
 * 频道排期中，有存在某天排期第一个节目未设置开始时间<br/>
 * <b>作者:</b>614<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年4月12日 下午5:09:55
 * @param date 
 */
public class ProgramStartTimeIsNullException extends BaseException{
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(ProgramStartTimeIsNullException.class);

	public ProgramStartTimeIsNullException(String date) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append(date).append("未设置节目开始时间").toString());
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append(date)
				.append("排期节目未设置开始时间")
				.toString());
	}
}
