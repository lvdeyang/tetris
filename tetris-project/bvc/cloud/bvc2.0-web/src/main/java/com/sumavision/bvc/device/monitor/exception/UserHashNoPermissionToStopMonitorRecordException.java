package com.sumavision.bvc.device.monitor.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class UserHashNoPermissionToStopMonitorRecordException extends BaseException{

	/** 这是一个常量的说明 */
	private static final long serialVersionUID = 1L;

	public UserHashNoPermissionToStopMonitorRecordException(Long recordId, Long userId) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("您没有权限删除此直播任务！用户id：")
															 .append(userId)
															 .append("，直播id：")
															 .append(recordId)
															 .toString());
	}

}
