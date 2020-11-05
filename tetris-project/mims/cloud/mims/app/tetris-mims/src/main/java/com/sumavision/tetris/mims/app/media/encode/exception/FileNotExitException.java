package com.sumavision.tetris.mims.app.media.encode.exception;


import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class FileNotExitException extends BaseException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static Logger LOG = LoggerFactory.getLogger(FileNotExitException.class);
	
	public FileNotExitException(String name) throws IOException{
		super(StatusCode.NOTFOUND, "文件不存在");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("文件")
				.append(name)
				.append("不存在")
				.toString());
	}

}
