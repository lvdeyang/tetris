package com.sumavision.tetris.bvc.model.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class AudioVideoTemplateNotFoundException extends BaseException{

	private static final long serialVersionUID = 1L;

	public AudioVideoTemplateNotFoundException(Long id) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("音视频参数不存在，id：")
															 .append(id)
															 .toString());
	}

}
