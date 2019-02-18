package com.sumavision.tetris.cms.template.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class TemplateNotExistException extends BaseException{

	private static final Logger LOG = LoggerFactory.getLogger(TemplateNotExistException.class);
	
	private static final long serialVersionUID = 1L;

	public TemplateNotExistException(Long id) {
		super(StatusCode.FORBIDDEN, "模板不存在！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("模板不存在!模板id：")
										   .append(id)
										   .toString());
	}

}
