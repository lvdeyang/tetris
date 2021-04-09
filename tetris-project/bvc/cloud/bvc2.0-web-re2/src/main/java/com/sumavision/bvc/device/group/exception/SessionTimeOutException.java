package com.sumavision.bvc.device.group.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

/**
 * session超时异常<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年10月25日 上午9:38:35
 */
public class SessionTimeOutException extends BaseException{

	private static final long serialVersionUID = 1L;

	public SessionTimeOutException() {
		super(StatusCode.TIMEOUT, "会话超时，请重新登录！");
	}

}
