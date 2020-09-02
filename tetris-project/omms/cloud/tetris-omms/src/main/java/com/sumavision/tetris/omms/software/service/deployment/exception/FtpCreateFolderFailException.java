package com.sumavision.tetris.omms.software.service.deployment.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class FtpCreateFolderFailException extends BaseException{

	private static final long serialVersionUID = 1L;

	public FtpCreateFolderFailException(String ip, String port, String folder) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("ftp创建文件夹失败：")
															 .append(ip)
															 .append("，")
															 .append(port)
															 .append("，")
															 .append(folder)
															 .toString());
	}

	
}
