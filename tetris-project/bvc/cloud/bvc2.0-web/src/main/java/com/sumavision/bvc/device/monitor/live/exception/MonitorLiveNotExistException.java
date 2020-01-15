package com.sumavision.bvc.device.monitor.live.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class MonitorLiveNotExistException extends BaseException{

	private static final long serialVersionUID = 1L;

	public MonitorLiveNotExistException(Long liveId) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("直播任务不存在！任务id：")
															 .append(liveId)
															 .toString());
	}

}
