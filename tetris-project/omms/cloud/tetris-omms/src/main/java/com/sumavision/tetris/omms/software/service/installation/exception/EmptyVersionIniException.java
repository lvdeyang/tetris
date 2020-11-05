package com.sumavision.tetris.omms.software.service.installation.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class EmptyVersionIniException extends BaseException{

	private static final long serialVersionUID = 1L;

	public EmptyVersionIniException() {
		super(StatusCode.FORBIDDEN, "安装包根目录下version.ini内容为空");
	}
	
}
