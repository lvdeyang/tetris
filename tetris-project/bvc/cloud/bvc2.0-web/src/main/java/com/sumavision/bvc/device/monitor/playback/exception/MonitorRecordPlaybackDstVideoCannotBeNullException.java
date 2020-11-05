package com.sumavision.bvc.device.monitor.playback.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class MonitorRecordPlaybackDstVideoCannotBeNullException extends BaseException{

	private static final long serialVersionUID = 1L;

	public MonitorRecordPlaybackDstVideoCannotBeNullException() {
		super(StatusCode.FORBIDDEN, "录制回放任务目的视频设备不能为空！");
	}

}
