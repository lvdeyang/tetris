package com.suma.venus.resource.base.bo;

/**
 * token操作响应结构体
 * @author Administrator
 *
 */
public class TokenResultBO {
	
	public static final int CODE_SUCCESS = 0;
	
	public static final int CODE_USERNAME_NOT_EXIST = 10001;
	
	public static final int CODE_PASSWORD_WRONG = 10002;
	
	public static final int CODE_TOKEN_INVALID = 20001;
	
	public static final int CODE_NO_AUTH = 30001;
	
	protected int status;
	
	protected String errorMessage;
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
}
