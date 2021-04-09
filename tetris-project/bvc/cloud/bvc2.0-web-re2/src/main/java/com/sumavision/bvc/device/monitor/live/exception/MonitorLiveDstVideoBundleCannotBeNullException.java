package com.sumavision.bvc.device.monitor.live.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class MonitorLiveDstVideoBundleCannotBeNullException extends BaseException{

	private static final long serialVersionUID = 1L;

	public MonitorLiveDstVideoBundleCannotBeNullException() {
		super(StatusCode.FORBIDDEN, "直播调阅任务目的视频设备不能为空！");
	}

}
