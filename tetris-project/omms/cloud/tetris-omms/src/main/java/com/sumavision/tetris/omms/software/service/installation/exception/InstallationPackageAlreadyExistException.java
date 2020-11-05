package com.sumavision.tetris.omms.software.service.installation.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class InstallationPackageAlreadyExistException extends BaseException{

	private static final long serialVersionUID = 1L;

	public InstallationPackageAlreadyExistException(String serviceTypeName, String fileName) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("安装包已经存在或与历史版本重名，服务：")
															 .append(serviceTypeName)
															 .append("，文件名：")
															 .append(fileName)
															 .toString());
	}

}
