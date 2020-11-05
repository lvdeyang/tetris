package com.sumavision.tetris.mims.app.material.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mims.app.material.MaterialType;

public class ErrorMaterialTypeException extends BaseException{

	private final Logger LOG = LoggerFactory.getLogger(ErrorMaterialTypeException.class);
	
	private static final long serialVersionUID = 1L;

	public ErrorMaterialTypeException(Long materialId, MaterialType type, MaterialType expect) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("当前素材不是")
															 .append(expect.getName())
															 .append("类型")
															 .toString());
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("素材类型不匹配, id:")
										   .append(materialId)
										   .append(", 类型:")
										   .append(type.getName())
										   .append(", 期望类型:")
										   .append(expect.getName())
										   .toString());
	}

}
