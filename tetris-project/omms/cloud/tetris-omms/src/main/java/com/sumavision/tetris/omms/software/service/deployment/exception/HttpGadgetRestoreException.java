/**
 * 
 */
package com.sumavision.tetris.omms.software.service.deployment.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class HttpGadgetRestoreException extends BaseException{
	private static final long serialVersionUID = 1L;

	public HttpGadgetRestoreException(String ip, String port, String responseStatus) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("http调用小工具恢复失败！")
															 .append(ip)
															 .append("，")
															 .append(port)
															 .append("，")
															 .append(responseStatus)
															 .toString());
	}
}