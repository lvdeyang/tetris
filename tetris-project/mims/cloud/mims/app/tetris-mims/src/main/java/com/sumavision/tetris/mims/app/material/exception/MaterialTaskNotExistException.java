package com.sumavision.tetris.mims.app.material.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class MaterialTaskNotExistException extends BaseException{

	private final Logger LOG = LoggerFactory.getLogger(MaterialTaskNotExistException.class);
	
	private static final long serialVersionUID = 1L;

	public MaterialTaskNotExistException(String uuid) {
		super(StatusCode.FORBIDDEN, "当前上传任务不存在！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("任务：")
										   .append(uuid)
										   .append(" 不存在！")
										   .toString());
	}
	
}
