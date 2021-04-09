package com.sumavision.bvc.device.monitor.live.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class NoAbilityToStartXtSeeLocalUserException extends BaseException{

	private static final long serialVersionUID = 1L;

	public NoAbilityToStartXtSeeLocalUserException(Long userId) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("当前xt用户点播本地用户能力用尽：")
															 .append("用户id：")
															 .append(userId)
															 .toString());
	}

}
