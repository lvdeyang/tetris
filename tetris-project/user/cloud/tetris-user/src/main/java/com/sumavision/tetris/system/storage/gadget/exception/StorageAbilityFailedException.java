package com.sumavision.tetris.system.storage.gadget.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class StorageAbilityFailedException extends BaseException{

	private static final long serialVersionUID = 1L;

	public StorageAbilityFailedException(String baseControlPath, String rootPath) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("存储能力获取失败！小工具地址：")
															 .append(baseControlPath)
															 .append("，存储根：")
															 .append(rootPath)
															 .toString());
	}

}
