package com.sumavision.tetris.mvc.ext.response.json.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

/**
 * 处理器有@JsonBody注解有contentLength=true配置时
 * 没有传入HttpServletResponse参数
 * lvdeyang 2018年01月10日
 */
public class NoResponseFoundForJsonBodyInContentLengthConfigureException extends BaseException{

	private static final long serialVersionUID = 1L;

	public NoResponseFoundForJsonBodyInContentLengthConfigureException(String handler) {
		super(StatusCode.ERROR, new StringBufferWrapper().append("当前处理器：")
														 .append(handler)
														 .append("没有找到HttpServletResponse参数，在@JsonBody中配置contentLength=true，参数是必要的！")
														 .toString());
	}
	
}
