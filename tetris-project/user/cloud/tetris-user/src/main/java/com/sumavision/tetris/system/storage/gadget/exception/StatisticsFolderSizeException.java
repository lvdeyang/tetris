package com.sumavision.tetris.system.storage.gadget.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class StatisticsFolderSizeException extends BaseException{

	private static final long serialVersionUID = 1L;

	public StatisticsFolderSizeException(String baseControlPath) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("统计文件夹大小获取失败！小工具地址：")
															 .append(baseControlPath)
															 .toString());
	}

}
