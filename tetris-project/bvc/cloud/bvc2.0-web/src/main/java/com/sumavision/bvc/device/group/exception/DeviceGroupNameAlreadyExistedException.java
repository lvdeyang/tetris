package com.sumavision.bvc.device.group.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class DeviceGroupNameAlreadyExistedException extends BaseException {

	public static final long serialVersionUID = 1L;
	
	public DeviceGroupNameAlreadyExistedException(String groupName){
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("设备组：")
															 .append(groupName)
															 .append("已经存在，请修改设备组名称或取消操作！")
															 .toString());
	}
	
}
