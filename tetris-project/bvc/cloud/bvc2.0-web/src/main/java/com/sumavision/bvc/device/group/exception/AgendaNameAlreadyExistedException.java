package com.sumavision.bvc.device.group.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class AgendaNameAlreadyExistedException extends BaseException{

	private static final long serialVersionUID = 1L;
	
	public AgendaNameAlreadyExistedException(Long groupId, String groupName, String agandaName){
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("当前设备组：")
															 .append(groupName)
															 .append("(id:")
															 .append(groupId)
															 .append("), 已存在议程：")
															 .append(agandaName)
															 .append("，请修改议程名称或取消操作！")
															 .toString());
	}
	
}
