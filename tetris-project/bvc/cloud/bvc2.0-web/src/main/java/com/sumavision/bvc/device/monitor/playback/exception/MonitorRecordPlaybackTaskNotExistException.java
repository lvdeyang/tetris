package com.sumavision.bvc.device.monitor.playback.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class MonitorRecordPlaybackTaskNotExistException extends BaseException{

	private static final long serialVersionUID = 1L;

	public MonitorRecordPlaybackTaskNotExistException(Long taskId) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("调阅任务不存在，任务id:")
															 .append(taskId)
															 .toString());
	}
	
}
