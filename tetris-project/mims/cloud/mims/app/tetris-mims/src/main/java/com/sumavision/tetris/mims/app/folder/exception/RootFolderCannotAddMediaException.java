package com.sumavision.tetris.mims.app.folder.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class RootFolderCannotAddMediaException extends BaseException{

	/** 这是一个常量的说明 */
	private static final long serialVersionUID = 1L;

	public RootFolderCannotAddMediaException() {
		super(StatusCode.FORBIDDEN, "请不要在根目录下添加媒资！");
	}

}
