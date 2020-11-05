package com.sumavision.bvc.device.monitor.live.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class MonitorLiveSrcAndDstCanNotBeTheSameException extends BaseException{

	private static final long serialVersionUID = 1L;

	public MonitorLiveSrcAndDstCanNotBeTheSameException() {
		super(StatusCode.FORBIDDEN, "直播调阅任务目的源和目的不能相同");
	}

}
