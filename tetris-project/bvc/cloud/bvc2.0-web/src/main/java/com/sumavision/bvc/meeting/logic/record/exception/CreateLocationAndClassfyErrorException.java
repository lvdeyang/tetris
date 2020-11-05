package com.sumavision.bvc.meeting.logic.record.exception;

import com.sumavision.bvc.system.enumeration.DicType;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class CreateLocationAndClassfyErrorException extends BaseException{
	
	public static final long serialVersionUID = 1L;
	
	public CreateLocationAndClassfyErrorException(DicType type, String errorMessage){
		
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("创建")
															 .append(type.getName())
															 .append("失败:")
															 .append(errorMessage).toString());
		
	}
}
