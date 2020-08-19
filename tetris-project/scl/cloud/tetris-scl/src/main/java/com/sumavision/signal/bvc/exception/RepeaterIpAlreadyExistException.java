package com.sumavision.signal.bvc.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class RepeaterIpAlreadyExistException extends BaseException{

	private static final long serialVersionUID = 1L;
	
	public RepeaterIpAlreadyExistException(String ip){
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("流转发器ip：")
															 .append(ip)
															 .append("已存在！")
															 .toString());
	}
}