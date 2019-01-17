package com.sumavision.tetris.easy.process.core.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ErrorAccessPointResponseStatusCodeException extends BaseException{

	private static final Logger LOG = LoggerFactory.getLogger(ErrorAccessPointResponseStatusCodeException.class);
	
	private static final long serialVersionUID = 1L;

	public ErrorAccessPointResponseStatusCodeException(String url, int statusCode){
		super(StatusCode.ERROR, new StringBufferWrapper().append("接入点调用失败, 状态码: ")
														 .append(statusCode)
														 .append(", url: ")
														 .append(url)
														 .toString());
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("接入点返回异常状态码: ")
										   .append(statusCode)
										   .append(", url: ")
										   .append(url)
										   .toString());
	}
	
	public ErrorAccessPointResponseStatusCodeException(String url, int statusCode, String message){
		super(StatusCode.ERROR, new StringBufferWrapper().append("接入点返回异常状态码: ")
														 .append(statusCode)
														 .append(", url: ")
														 .append(url)
														 .append(", 接口返回信息：")
														 .append(message)
														 .toString());
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("接入点返回异常状态码: ")
										   .append(statusCode)
										   .append(", url: ")
										   .append(url)
										   .append(", 接口返回信息：")
										   .append(message)
										   .toString());
	}

}
