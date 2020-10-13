package com.sumavision.tetris.omms.software.service.deployment.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class FtpLoginFailWhenUploadInstallationPackageException extends BaseException{

	private static final long serialVersionUID = 1L;

	public FtpLoginFailWhenUploadInstallationPackageException(String ip, String port, String username, String password) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("安装包上传时ftp登录失败：")
															 .append(ip)
															 .append("，")
															 .append(ip)
															 .append("，")
															 .append(port)
															 .append("，")
															 .append(username)
															 .append("，")
															 .append(password)
															 .toString());
	}

}
