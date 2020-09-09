package com.sumavision.bvc.device.monitor.live.exception;

import com.suma.venus.resource.constant.BusinessConstants;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class UserHasNoPermissionForBusinessException extends BaseException{

	private static final long serialVersionUID = 1L;

	public UserHasNoPermissionForBusinessException(BusinessConstants.BUSINESS_OPR_TYPE business, int targetType) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("您没有")
															 .append(business.getName())
															 .append("当前")
															 .append(targetType==0?"设备":"用户")
															 .append("的权限")
															 .toString());
	}
	
	public UserHasNoPermissionForBusinessException(BusinessConstants.BUSINESS_OPR_TYPE business, String name, int targetType) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("您没有")
															 .append(business.getName())
															 .append(name)
															 .append(targetType==0?"设备":"用户")
															 .append("的权限")
															 .toString());
	}

	public UserHasNoPermissionForBusinessException(BusinessConstants.BUSINESS_OPR_TYPE business,int targetType,String names){
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("您没有")
				 .append(business.getName())
				 .append(names)
				 .append(targetType==0?"设备":"用户")
				 .append("的权限")
				 .toString());
	}
	
}
