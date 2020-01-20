package com.sumavision.tetris.omms.software.service.type.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class NoServiceTypesToAddException extends BaseException{

	private static final long serialVersionUID = 1L;

	public NoServiceTypesToAddException(){
		super(StatusCode.FORBIDDEN, "没有可以添加的服务类型!");
	}

}
