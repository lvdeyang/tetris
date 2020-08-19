package com.sumavision.signal.bvc.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class RepeaterNameAlreadyExistException extends BaseException{

	private static final long serialVersionUID = 1L;
	
	public RepeaterNameAlreadyExistException(String name){
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("流转发器名称：")
															 .append(name)
															 .append("已存在！")
															 .toString());
	}
}
