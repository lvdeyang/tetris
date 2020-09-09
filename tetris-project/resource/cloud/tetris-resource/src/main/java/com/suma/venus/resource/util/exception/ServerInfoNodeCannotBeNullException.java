package com.suma.venus.resource.util.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class ServerInfoNodeCannotBeNullException extends BaseException{

	private static final long serialVersionUID = 1L;

	public ServerInfoNodeCannotBeNullException() {
		super(StatusCode.FORBIDDEN, "请填写服务节点厂商信息！");
	}

}
