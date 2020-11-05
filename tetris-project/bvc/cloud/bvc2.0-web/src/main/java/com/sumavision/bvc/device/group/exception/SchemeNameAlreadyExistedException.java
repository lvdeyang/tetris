package com.sumavision.bvc.device.group.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class SchemeNameAlreadyExistedException extends BaseException{

	private static final long serialVersionUID = 1L;
	
	public SchemeNameAlreadyExistedException(Long groupId, String groupName, String schemeName){
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("当前设备组：")
															 .append(groupName)
															 .append("(id:")
															 .append(groupId)
															 .append("), 已存在方案：")
															 .append(schemeName)
															 .append("，请修改方案名称或取消操作！")
															 .toString());
	}
	
	
	
}
