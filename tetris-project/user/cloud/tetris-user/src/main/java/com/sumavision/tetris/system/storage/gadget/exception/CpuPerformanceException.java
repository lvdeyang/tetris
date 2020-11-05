package com.sumavision.tetris.system.storage.gadget.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class CpuPerformanceException extends BaseException{

	private static final long serialVersionUID = 1L;

	public CpuPerformanceException(String baseControlPath) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("cpu信息获取失败！小工具地址：")
				 											 .append(baseControlPath)
				 											 .toString());
	}
	
}
