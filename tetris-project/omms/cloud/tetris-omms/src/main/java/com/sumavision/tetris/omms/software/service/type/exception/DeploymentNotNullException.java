package com.sumavision.tetris.omms.software.service.type.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;

public class DeploymentNotNullException extends BaseException{

	private static final long serialVersionUID = 1L;

	public DeploymentNotNullException(){
		super(StatusCode.FORBIDDEN, "已存在部署服务，不可删除该服务!");
	}

}
