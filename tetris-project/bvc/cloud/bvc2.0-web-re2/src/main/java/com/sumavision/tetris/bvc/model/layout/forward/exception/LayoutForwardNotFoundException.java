package com.sumavision.tetris.bvc.model.layout.forward.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class LayoutForwardNotFoundException extends BaseException{

	private static final long serialVersionUID = 1L;

	public LayoutForwardNotFoundException(Long id) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("转发配置不存在，id：")
															 .append(id)
															 .toString());
	}

}
