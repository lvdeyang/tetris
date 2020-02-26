package com.sumavision.bvc.device.command.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class CommandGroupNameAlreadyExistedException extends BaseException {

	public static final long serialVersionUID = 1L;
	
	public CommandGroupNameAlreadyExistedException(String groupName){
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("会议：")
															 .append(groupName)
															 .append("已经存在，请修改名称或取消操作！")
															 .toString());
	}
	
}
