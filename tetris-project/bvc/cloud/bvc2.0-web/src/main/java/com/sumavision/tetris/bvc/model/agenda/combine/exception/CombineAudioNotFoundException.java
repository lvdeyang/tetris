package com.sumavision.tetris.bvc.model.agenda.combine.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class CombineAudioNotFoundException extends BaseException{

	private static final long serialVersionUID = 1L;

	public CombineAudioNotFoundException(Long id) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("混音不存在，id：")
															 .append(id)
															 .toString());
	}

}
