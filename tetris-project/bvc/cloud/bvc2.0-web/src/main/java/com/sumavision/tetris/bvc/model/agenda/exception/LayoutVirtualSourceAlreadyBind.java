package com.sumavision.tetris.bvc.model.agenda.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class LayoutVirtualSourceAlreadyBind extends BaseException{

	private static final long serialVersionUID = 1L;

	public LayoutVirtualSourceAlreadyBind() {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("已绑定资源！") .toString());
	}
}
