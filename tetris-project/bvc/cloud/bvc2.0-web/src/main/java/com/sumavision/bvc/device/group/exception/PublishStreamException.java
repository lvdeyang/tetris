package com.sumavision.bvc.device.group.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class PublishStreamException extends BaseException{

	private static final long serialVersionUID = 1L;
	
	public PublishStreamException(String msg){
		super(StatusCode.FORBIDDEN, msg);
	}
	
}
