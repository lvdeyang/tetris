package com.sumavision.tetris.omms.software.service.deployment.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class HttpGadgetEquipmentIdentificationCodeException extends BaseException{
	private static final long serialVersionUID = 1L;

	public HttpGadgetEquipmentIdentificationCodeException(String ip, String port, String responseStatus) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("http调用小工具获取设备标识码失败！")
															 .append(ip)
															 .append("，")
															 .append(port)
															 .append("，")
															 .append(responseStatus)
															 .toString());
	}

}
