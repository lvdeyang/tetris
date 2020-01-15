package com.sumavision.tetris.commons.exception;

import com.sumavision.tetris.commons.exception.code.StatusCode;

/**
 * 异常基类
 * lvdeyang 2017年6月25日
 */
public class BaseException extends Exception{

	private static final long serialVersionUID = 1L;
	
	//协议码
	private String protocol;
	
	//状态码
	private StatusCode code;
	
	//重定向路径
	private String forwardPath;
	
	public BaseException(StatusCode code, String message){
		super(message);
		this.code = code;
	}
	
	public BaseException(StatusCode code, String message, String forwardPath){
		super(message);
		this.code = code;
		this.forwardPath = forwardPath;
	}
	
	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public StatusCode getCode(){
		return this.code;
	}

	public String getForwardPath() {
		return forwardPath;
	}
	
}