package com.sumavision.bvc.device.monitor.vod.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class UserHasNoPermissionForExternalFolderException extends BaseException{

	private static final long serialVersionUID = 1L;

	public UserHasNoPermissionForExternalFolderException() {
		super(StatusCode.FORBIDDEN, "您没有权限操作此外部文件夹！");
	}

}
