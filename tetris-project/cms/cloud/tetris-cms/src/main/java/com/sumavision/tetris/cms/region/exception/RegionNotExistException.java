package com.sumavision.tetris.cms.region.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class RegionNotExistException extends BaseException{
	
	private static final Logger LOG = LoggerFactory.getLogger(RegionNotExistException.class);
	
	private static final long serialVersionUID = 1L;

	public RegionNotExistException(Long id) {
		super(StatusCode.FORBIDDEN, "地区不存在！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("地区不存在!模板id：")
										   .append(id)
										   .toString());
	}
}
