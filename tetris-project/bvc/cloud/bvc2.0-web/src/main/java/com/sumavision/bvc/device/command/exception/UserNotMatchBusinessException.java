package com.sumavision.bvc.device.command.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class UserNotMatchBusinessException extends BaseException{
	
	private static final long serialVersionUID = 1L;

	public UserNotMatchBusinessException(String userName, Long businessId, String business) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("用户和业务不匹配，用户为：")
															  .append(userName)
															  .append("，业务为：")
															  .append(business)
															  .append("，业务id为：")
															  .append(businessId)
															  .toString());
	}

}
