package com.sumavision.bvc.device.monitor.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class UserHasNoPermissionToRemoveMonitorRecordPlaybackTaskException extends BaseException{

	private static final long serialVersionUID = 1L;

	public UserHasNoPermissionToRemoveMonitorRecordPlaybackTaskException(Long taskId, Long userId) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("您没有权限删除此调阅任务！用户id：")
															 .append(userId)
															 .append("，调阅任务id：")
															 .append(taskId)
															 .toString());
	}

}
