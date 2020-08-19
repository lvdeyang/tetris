package com.sumavision.bvc.device.monitor.vod.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ExternalFolderDonnotExistException extends BaseException{

	private static final long serialVersionUID = 1L;

	public ExternalFolderDonnotExistException(Long id) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("外部文件夹不存在：")
															 .append(id)
															 .toString());
	}
	
}
