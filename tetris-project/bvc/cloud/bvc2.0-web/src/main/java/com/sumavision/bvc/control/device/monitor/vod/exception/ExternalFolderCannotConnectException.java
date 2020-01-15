package com.sumavision.bvc.control.device.monitor.vod.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ExternalFolderCannotConnectException extends BaseException{

	private static final long serialVersionUID = 1L;

	public ExternalFolderCannotConnectException(String fullPath) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("文件夹：")
															 .append(fullPath)
															 .append("无法访问！")
															 .toString());
	}

}
