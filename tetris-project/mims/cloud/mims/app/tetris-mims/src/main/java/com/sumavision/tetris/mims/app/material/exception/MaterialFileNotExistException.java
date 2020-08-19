package com.sumavision.tetris.mims.app.material.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class MaterialFileNotExistException extends BaseException{

	private final Logger LOG = LoggerFactory.getLogger(MaterialFileNotExistException.class);
	
	private static final long serialVersionUID = 1L;

	public MaterialFileNotExistException(Long id) {
		super(StatusCode.FORBIDDEN, "当前素材文件不存在！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("素材文件不存在，id：")
										   .append(id)
										   .toString());
	}
	
}
