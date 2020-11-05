package com.sumavision.bvc.device.group.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class TplIsNullException extends BaseException{

	public static final long serialVersionUID = 1L;
	
	public TplIsNullException(){
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("会议模板为空，请取消操作，重新创建！").toString());
	}
	
}
