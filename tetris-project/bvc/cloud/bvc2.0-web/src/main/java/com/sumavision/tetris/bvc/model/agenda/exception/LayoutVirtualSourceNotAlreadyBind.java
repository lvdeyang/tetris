package com.sumavision.tetris.bvc.model.agenda.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class LayoutVirtualSourceNotAlreadyBind extends BaseException{

	private static final long serialVersionUID = 1L;

	public LayoutVirtualSourceNotAlreadyBind() {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("未绑定资源！") .toString());
	}
}
