package com.sumavision.signal.bvc.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class InternetAccessAddressAlreadyExistException extends BaseException{
	
	private static final long serialVersionUID = 1L;

	public InternetAccessAddressAlreadyExistException(String address) {
		super(StatusCode.FORBIDDEN,  new StringBufferWrapper().append("网口地址：")
															  .append(address)
															  .append("已存在！")
															  .toString());
	}

}
