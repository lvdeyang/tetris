package com.sumavision.bvc.device.group.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class DeviceGroupAlreadyStartedException extends BaseException{
	
	private static final long serialVersionUID = 1L;

	public DeviceGroupAlreadyStartedException(Long groupId, String gorupName) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("当前设备组：")
															 .append(gorupName)
															 .append("(id:")
															 .append(groupId)
															 .append("), 已经开始，无法进行当前操作！")
															 .toString());
	}

	
	
}
