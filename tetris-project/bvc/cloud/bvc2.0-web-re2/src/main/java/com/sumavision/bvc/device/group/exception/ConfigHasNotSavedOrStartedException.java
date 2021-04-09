package com.sumavision.bvc.device.group.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ConfigHasNotSavedOrStartedException extends BaseException{

	private static final long serialVersionUID = 1L;

	public ConfigHasNotSavedOrStartedException() {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("请先保存并执行该配置")
															 .toString());
	}
	
}
