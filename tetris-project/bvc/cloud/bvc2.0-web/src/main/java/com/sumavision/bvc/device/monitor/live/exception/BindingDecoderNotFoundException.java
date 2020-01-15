package com.sumavision.bvc.device.monitor.live.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class BindingDecoderNotFoundException extends BaseException{

	private static final long serialVersionUID = 1L;

	public BindingDecoderNotFoundException(String username) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("用户缺失绑定的编码器！")
															 .append(username)
															 .toString());
	}

}
