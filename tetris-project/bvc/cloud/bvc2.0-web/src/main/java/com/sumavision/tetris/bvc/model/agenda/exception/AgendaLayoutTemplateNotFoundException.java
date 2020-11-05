package com.sumavision.tetris.bvc.model.agenda.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class AgendaLayoutTemplateNotFoundException extends BaseException{

	private static final long serialVersionUID = 1L;

	public AgendaLayoutTemplateNotFoundException(Long id) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("布局信息不存在，id：")
															 .append(id)
															 .toString());
	}

}
