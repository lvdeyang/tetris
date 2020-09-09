package com.sumavision.tetris.bvc.business.terminal.hall.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ConferenceHallNotFoundException extends BaseException{

	private static final long serialVersionUID = 1L;

	public ConferenceHallNotFoundException(Long id) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("会场不存在，id：").append(id).toString());
	}

}
