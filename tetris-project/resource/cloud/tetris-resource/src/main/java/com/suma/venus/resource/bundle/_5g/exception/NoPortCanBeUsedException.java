package com.suma.venus.resource.bundle._5g.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class NoPortCanBeUsedException extends BaseException{

	private static final long serialVersionUID = 1L;

	public NoPortCanBeUsedException() {
		super(StatusCode.FORBIDDEN, "没有可用的5G代理服务端口");
	}

}
