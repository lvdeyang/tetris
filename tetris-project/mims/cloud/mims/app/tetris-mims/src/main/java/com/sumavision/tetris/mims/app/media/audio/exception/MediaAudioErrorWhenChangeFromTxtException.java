package com.sumavision.tetris.mims.app.media.audio.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class MediaAudioErrorWhenChangeFromTxtException extends BaseException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = LoggerFactory.getLogger(MediaAudioErrorWhenChangeFromTxtException.class);
	
	public MediaAudioErrorWhenChangeFromTxtException(String name) {
		super(StatusCode.ERROR, "转换文件失败");
		LOG.error(DateUtil.now());
		LOG.error(new StringBufferWrapper().append("转换文本:")
				.append(name)
				.append("失败")
				.toString());
	}
}
