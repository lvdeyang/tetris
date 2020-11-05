package com.sumavision.tetris.cms.template.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class TemplateTagMoveFailException extends BaseException{

	private static final Logger LOG = LoggerFactory.getLogger(TemplateTagMoveFailException.class);
	
	/** 这是一个常量的说明 */
	private static final long serialVersionUID = 1L;

	public TemplateTagMoveFailException(Long sourceId, Long targetId) {
		super(StatusCode.FORBIDDEN, "标签移动失败！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("标签移动失败！待移动标签id:")
										   .append(sourceId)
										   .append("移动目标标签id:")
										   .append(targetId)
										   .toString());
	}

}
