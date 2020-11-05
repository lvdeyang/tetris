package com.sumavision.tetris.mims.app.media.tag.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class TagHasExistsException extends BaseException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Logger LOG = LoggerFactory.getLogger(TagHasExistsException.class);
	
	public TagHasExistsException(String name) {
		super(StatusCode.CONFLICT, "该标签已存在");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("标签")
				.append(name)
				.append("已存在")
				.toString());
	}
}
