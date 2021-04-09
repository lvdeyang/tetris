package com.sumavision.tetris.bvc.model.layout.forward.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class CombineTemplatePositionNotFoundException extends BaseException{

	private static final long serialVersionUID = 1L;

	public CombineTemplatePositionNotFoundException(Long id) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("合屏模板布局不存在，id：")
															 .append(id)
															 .toString());
	}

}
