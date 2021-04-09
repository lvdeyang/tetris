package com.sumavision.bvc.meeting.logic.execption;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ExecuteBusinessException extends BaseException{

	private static final long serialVersionUID = 1L;
	
	public ExecuteBusinessException(String errMsg){
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("执行失败：")
															 .append(errMsg)
															 .toString());
	}
	
}
