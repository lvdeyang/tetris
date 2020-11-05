package com.sumavision.tetris.easy.process.access.point.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ParamReferencedByJointConstraintsException extends BaseException{

	private static final Logger LOG = LoggerFactory.getLogger(ParamReferencedByJointConstraintsException.class);
	
	private static final long serialVersionUID = 1L;

	public ParamReferencedByJointConstraintsException(String primaryKey) {
		super(StatusCode.FORBIDDEN, "参数被联合约束引用！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("参数呗联合约束引用！参数主键：")
										   .append(primaryKey)
										   .toString());
	}

}
