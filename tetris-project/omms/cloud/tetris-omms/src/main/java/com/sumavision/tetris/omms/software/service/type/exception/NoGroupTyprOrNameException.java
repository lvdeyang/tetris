package com.sumavision.tetris.omms.software.service.type.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class NoGroupTyprOrNameException extends BaseException{

	private static final long serialVersionUID = 1L;

	public NoGroupTyprOrNameException(){
//		super(StatusCode.FORBIDDEN, "请选择服务类型或输入合法的名称!");
		super(StatusCode.FORBIDDEN, "请输入服务名称!");
	}

}
