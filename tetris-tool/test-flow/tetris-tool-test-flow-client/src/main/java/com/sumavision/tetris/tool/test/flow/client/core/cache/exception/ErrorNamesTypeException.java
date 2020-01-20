package com.sumavision.tetris.tool.test.flow.client.core.cache.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

/**
 * @ClassName: 缓存名枚举异常<br/> 
 * @author lvdeyang
 * @date 2018年8月29日 下午5:03:06 
 */
public class ErrorNamesTypeException extends BaseException{

	private static final long serialVersionUID = 1L;

	public ErrorNamesTypeException(String name) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("错误的缓存名：")
															 .append(name)
															 .toString());
	}
	
}
