package com.suma.venus.resource.bundle._5g.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class BundleIdUsedByOtherDeviceModelException extends BaseException{

	private static final long serialVersionUID = 1L;

	public BundleIdUsedByOtherDeviceModelException(String bundleId) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("5G背包的序列号被其他设备占用，bundleId：")
															 .append(bundleId)
															 .toString());
	}
	
}
