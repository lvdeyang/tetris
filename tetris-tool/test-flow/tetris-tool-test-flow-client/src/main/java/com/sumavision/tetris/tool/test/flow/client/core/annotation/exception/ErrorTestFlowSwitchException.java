package com.sumavision.tetris.tool.test.flow.client.core.annotation.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ErrorTestFlowSwitchException extends BaseException{

	private static final long serialVersionUID = 1L;

	public ErrorTestFlowSwitchException(String name) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("无法识别的测试状态：")
														     .append(name)
														     .toString());
	}
	
}
