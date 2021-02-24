package com.sumavision.tetris.system.role.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class AutoGenerationRoleCannotDeleteException extends BaseException{

	/** 这是一个常量的说明 */
	private static final long serialVersionUID = 1L;

	public AutoGenerationRoleCannotDeleteException() {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("内置系统角色不能删除！").toString());
	}

}
