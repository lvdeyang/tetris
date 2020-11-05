package com.sumavision.tetris.commons.exception;

import com.sumavision.tetris.commons.util.I18nUtil;

public class CommonException extends RuntimeException
{
	private static final long serialVersionUID = 6018197048899000330L;
	String errCode = "sys_10001";
	
	@Override
	public String getMessage() {
		return super.getMessage()==null||super.getMessage().isEmpty()?I18nUtil.i18n("sys_10001"):super.getMessage();
	}

	public CommonException(Throwable cause) {
		super(cause.getMessage());
		if (cause instanceof CommonException){
			CommonException err = (CommonException) cause;
			errCode = err.getErrCode();
		}
	}
	
	public CommonException(Err err){
		super(err.getErrMsg());
		errCode = err.getErrCode();
	}
	
	public CommonException(String errCode,String errMsg){
		super(errMsg);
		this.errCode = errCode;
	}

	public String getErrCode() {
		return errCode;
	}
	
	
}