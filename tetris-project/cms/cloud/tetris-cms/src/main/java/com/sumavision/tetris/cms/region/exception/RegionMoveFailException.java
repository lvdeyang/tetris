package com.sumavision.tetris.cms.region.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class RegionMoveFailException extends BaseException{
	
	private static final Logger LOG = LoggerFactory.getLogger(RegionMoveFailException.class);
	
	/** 这是一个常量的说明 */
	private static final long serialVersionUID = 1L;

	public RegionMoveFailException(Long sourceId, Long targetId) {
		super(StatusCode.FORBIDDEN, "地区移动失败！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("地区移动失败！待移动地区id:")
										   .append(sourceId)
										   .append("移动目标地区id:")
										   .append(targetId)
										   .toString());
	}
}
