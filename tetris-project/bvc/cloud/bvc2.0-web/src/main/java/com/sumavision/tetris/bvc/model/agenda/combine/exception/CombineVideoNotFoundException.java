package com.sumavision.tetris.bvc.model.agenda.combine.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class CombineVideoNotFoundException extends BaseException{

	private static final long serialVersionUID = 1L;

	public CombineVideoNotFoundException(Long id) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("合屏不存在，id：")
															 .append(id)
															 .toString());
	}

}
