package com.sumavision.tetris.patrol.address.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class AddressNotFoundException extends BaseException{

	private static final long serialVersionUID = 1L;

	public AddressNotFoundException(String uuid) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("错误签到地址，")
															 .append(uuid)
															 .toString());
	}

}
