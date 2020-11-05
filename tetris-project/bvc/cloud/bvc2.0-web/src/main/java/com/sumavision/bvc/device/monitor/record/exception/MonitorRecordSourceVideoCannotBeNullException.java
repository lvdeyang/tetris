package com.sumavision.bvc.device.monitor.record.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class MonitorRecordSourceVideoCannotBeNullException extends BaseException{

	private static final long serialVersionUID = 1L;

	public MonitorRecordSourceVideoCannotBeNullException() {
		super(StatusCode.FORBIDDEN, "录制不能没有视频源！");
	}

}
