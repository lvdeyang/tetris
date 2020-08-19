package com.sumavision.tetris.streamTranscoding.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class HttpRequestErrorException extends BaseException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(HttpRequestErrorException.class);
	
	public HttpRequestErrorException(String action) {
		super(StatusCode.ERROR, "请求小工具失败");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("请求动作：")
				.append(action)
				.append("小工具返回失败")
				.toString());
	}
}
