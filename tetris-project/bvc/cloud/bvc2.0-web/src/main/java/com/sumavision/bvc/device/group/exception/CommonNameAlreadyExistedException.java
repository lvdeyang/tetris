package com.sumavision.bvc.device.group.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class CommonNameAlreadyExistedException extends BaseException {

	public static final long serialVersionUID = 1L;
	
	public CommonNameAlreadyExistedException(String itemName, String name){
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append(itemName + "：")
															 .append(name)
															 .append("已经存在，请修改名称或取消操作！")
															 .toString());
	}
	
}
