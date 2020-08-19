package com.sumavision.tetris.mims.app.media.tag.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class TagNotExistsException extends BaseException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Logger LOG = LoggerFactory.getLogger(TagNotExistsException.class);
	
	public TagNotExistsException(Long id) {
		super(StatusCode.NOTFOUND, "标签不存在");
		
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("标签:")
				.append(id)
				.append("不存在")
				.toString());
	}
}
