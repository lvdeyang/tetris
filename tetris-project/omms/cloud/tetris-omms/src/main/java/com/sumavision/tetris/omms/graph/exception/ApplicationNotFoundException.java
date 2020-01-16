package com.sumavision.tetris.omms.graph.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ApplicationNotFoundException extends BaseException{

	private static final long serialVersionUID = 1L;

	public ApplicationNotFoundException(String instanceId) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("服务实例不存在：").append(instanceId).toString());
	}

}
