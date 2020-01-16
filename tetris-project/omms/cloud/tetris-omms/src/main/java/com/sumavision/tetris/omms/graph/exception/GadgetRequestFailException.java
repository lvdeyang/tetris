package com.sumavision.tetris.omms.graph.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class GadgetRequestFailException extends BaseException{

	private static final long serialVersionUID = 1L;

	public GadgetRequestFailException(String ip, String port) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("小工具请求失败，服务器ip：")
															 .append(ip)
															 .append("，请求端口：")
															 .append(port)
															 .toString());
	}

}
