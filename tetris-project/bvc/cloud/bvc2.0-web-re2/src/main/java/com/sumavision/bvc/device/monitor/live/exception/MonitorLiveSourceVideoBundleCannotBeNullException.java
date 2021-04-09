package com.sumavision.bvc.device.monitor.live.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class MonitorLiveSourceVideoBundleCannotBeNullException extends BaseException{

	private static final long serialVersionUID = 1L;

	public MonitorLiveSourceVideoBundleCannotBeNullException() {
		super(StatusCode.FORBIDDEN, "直播调阅任务视频源不能为空！");
	}

}
