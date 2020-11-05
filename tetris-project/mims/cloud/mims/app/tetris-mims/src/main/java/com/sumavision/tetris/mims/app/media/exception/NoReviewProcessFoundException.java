package com.sumavision.tetris.mims.app.media.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class NoReviewProcessFoundException extends BaseException{

	private static final long serialVersionUID = 1L;

	public NoReviewProcessFoundException(String processName) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("当前系统已经开启审核，但缺少")
															 .append(processName)
															 .append("，请联系管理员！")
															 .toString());
	}
	
	public NoReviewProcessFoundException(Long processId, String processName) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("当前系统已经开启审核，但未获取到")
															 .append(processName)
															 .append("（")
															 .append(processId)
															 .append("），请联系管理员！")
															 .toString());
	}

}
