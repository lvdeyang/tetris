package com.sumavision.tetris.easy.process.access.point.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class AccessPointCannotDeleteException extends BaseException{

	private static final Logger LOG = LoggerFactory.getLogger(AccessPointCannotDeleteException.class);
	
	private static final long serialVersionUID = 1L;

	public AccessPointCannotDeleteException(Long id) {
		super(StatusCode.FORBIDDEN, "当前接入点有其他流程引用，无法删除！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("接入点不能删除，id：")
										   .append(id)
										   .toString());
	}

}
