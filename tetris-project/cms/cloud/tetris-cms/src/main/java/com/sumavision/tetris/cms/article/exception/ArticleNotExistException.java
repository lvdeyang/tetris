package com.sumavision.tetris.cms.article.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ArticleNotExistException extends BaseException{

	private static final Logger LOG = LoggerFactory.getLogger(ArticleNotExistException.class);
	
	private static final long serialVersionUID = 1L;

	public ArticleNotExistException(Long id) {
		super(StatusCode.FORBIDDEN, "文章不存在！");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("文章不存在！id：")
										   .append(id)
										   .toString());
	}

}
