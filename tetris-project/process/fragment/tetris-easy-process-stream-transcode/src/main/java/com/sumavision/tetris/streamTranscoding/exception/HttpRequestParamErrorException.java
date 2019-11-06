package com.sumavision.tetris.streamTranscoding.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class HttpRequestParamErrorException extends BaseException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(HttpRequestParamErrorException.class);
	
	public HttpRequestParamErrorException(String param){
		super(StatusCode.ERROR, "请求参数错误");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("请求参数：")
				.append(param)
				.append(" 不合法")
				.toString());
	}
}
