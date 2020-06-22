package com.sumavision.tetris.bvc.model.terminal.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class TerminalBundleChannelNotFoundException extends BaseException{

	/** 这是一个常量的说明 */
	private static final long serialVersionUID = 1L;

	public TerminalBundleChannelNotFoundException(Long id) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("终端设备通道不存在，id：")
															 .append(id)
															 .toString());
	}

	public TerminalBundleChannelNotFoundException(Long terminalBundleId, String channelId) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("终端设备通道不存在，terminalBundleId：")
															 .append(terminalBundleId)
															 .append("，channelId：")
															 .append(channelId)
															 .toString());
	}
	
}
