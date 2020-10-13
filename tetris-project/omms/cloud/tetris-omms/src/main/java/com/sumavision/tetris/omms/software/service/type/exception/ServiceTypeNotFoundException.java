package com.sumavision.tetris.omms.software.service.type.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ServiceTypeNotFoundException extends BaseException{

	private static final long serialVersionUID = 1L;

	public ServiceTypeNotFoundException(Long id) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("服务类型不存在，id：").append(id).toString());
	}

}
