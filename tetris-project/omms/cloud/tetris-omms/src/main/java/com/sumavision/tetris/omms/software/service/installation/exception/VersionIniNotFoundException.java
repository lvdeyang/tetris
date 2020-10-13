package com.sumavision.tetris.omms.software.service.installation.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class VersionIniNotFoundException extends BaseException{

	private static final long serialVersionUID = 1L;

	public VersionIniNotFoundException() {
		super(StatusCode.FORBIDDEN, "安装包根路径下没找到version.ini文件");
	}

}
