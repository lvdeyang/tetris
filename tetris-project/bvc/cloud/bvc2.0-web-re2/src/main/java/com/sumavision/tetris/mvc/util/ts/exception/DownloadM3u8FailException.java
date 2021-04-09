package com.sumavision.tetris.mvc.util.ts.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class DownloadM3u8FailException extends BaseException{

	/** 这是一个常量的说明 */
	private static final long serialVersionUID = 1L;

	public DownloadM3u8FailException(Long userId, String m3u8HttpPath, String errorMessage) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("下载m3u8文件失败，")
															 .append("用户：").append(userId).append("，")
															 .append("m3u8地址：").append(m3u8HttpPath).append("，")
															 .append("服务器返回信息：").append(errorMessage)
															 .toString());
	}

}
