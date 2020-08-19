package com.sumavision.bvc.device.group.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class DeviceGroupHasNotStartedException extends BaseException{

	private static final long serialVersionUID = 1L;

	public DeviceGroupHasNotStartedException(Long groupId, String gorupName) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("当前设备组：")
															 .append(gorupName)
															 .append("(id:")
															 .append(groupId)
															 .append("), 已停止，请先执行开始操作！")
															 .toString());
	}
	
}
