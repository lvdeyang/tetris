package com.sumavision.tetris.cms.classify.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ClassifyNotExistException extends BaseException{

	private static final Logger LOG = LoggerFactory.getLogger(ClassifyNotExistException.class);
	
	private static final long serialVersionUID = 1L;

	public ClassifyNotExistException(Long id) {
		super(StatusCode.FORBIDDEN, "分类不存在！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("分类不存在！id：")
										   .append(id)
										   .toString());
	}

}
