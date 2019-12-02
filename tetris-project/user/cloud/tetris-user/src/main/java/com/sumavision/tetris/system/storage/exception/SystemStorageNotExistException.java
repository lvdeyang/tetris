package com.sumavision.tetris.system.storage.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class SystemStorageNotExistException extends BaseException{

	private static final long serialVersionUID = 1L;

	public SystemStorageNotExistException(Long id, String name) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("存储数据丢失！id：")
															 .append(id)
															 .append("，名称：")
															 .append(name)
															 .toString());
	}

}
