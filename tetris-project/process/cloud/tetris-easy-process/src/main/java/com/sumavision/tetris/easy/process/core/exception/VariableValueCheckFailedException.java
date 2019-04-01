package com.sumavision.tetris.easy.process.core.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

/**
 * 变量校验未通过<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月9日 下午2:24:45
 */
public class VariableValueCheckFailedException extends BaseException{

	private static final Logger LOG = LoggerFactory.getLogger(VariableValueCheckFailedException.class);
	
	private static final long serialVersionUID = 1L;

	public VariableValueCheckFailedException(
			String primaryKey, 
			String name, 
			String value, 
			String expression) {
		
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("校验未通过！")
															 .append("变量：")
															 .append(primaryKey)
															 .append("（")
															 .append(name)
															 .append("）")
															 .append("值：")
															 .append(value)
															 .append("约束：")
															 .append(expression)
															 .toString());
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("校验未通过！")
										   .append("变量：")
										   .append(primaryKey)
										   .append("（")
										   .append(name)
										   .append("）")
										   .append("值：")
										   .append(value)
										   .append("约束：")
										   .append(expression)
										   .toString());
	}

}
