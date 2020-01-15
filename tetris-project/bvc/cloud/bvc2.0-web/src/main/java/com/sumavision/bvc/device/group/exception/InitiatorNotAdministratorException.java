package com.sumavision.bvc.device.group.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

/**
 * 多人通话命令发起人不是管理员异常<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年11月11日 下午4:31:04
 */
public class InitiatorNotAdministratorException extends BaseException{

	private static final long serialVersionUID = 1L;

	public InitiatorNotAdministratorException(String bundleName) {
		super(StatusCode.ERROR, new StringBufferWrapper().append("用户/设备：“")
														 .append(bundleName)
														 .append("”不是建会人！")
														 .toString());
	}
}
