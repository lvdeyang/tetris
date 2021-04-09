package com.sumavision.bvc.control.system.exceptions;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class NoDelException extends BaseException{

	public NoDelException(long id) {
		super(StatusCode.ERROR, new StringBufferWrapper().append("DB删除异常，id:").append(id).toString());
	}
	public NoDelException(String status) {
		super(StatusCode.ERROR, new StringBufferWrapper().append("DB删除异常，全部删除:").append(status).toString());
	}
	
	
}
