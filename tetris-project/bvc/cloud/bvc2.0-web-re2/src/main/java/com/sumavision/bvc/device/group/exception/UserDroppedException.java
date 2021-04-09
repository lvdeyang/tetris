package com.sumavision.bvc.device.group.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

/**
 * 用户登录后只能通过username向用户服务查询用户信息，如果查询不到抛该异常<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年10月19日 下午4:31:04
 */
public class UserDroppedException extends BaseException{

	private static final long serialVersionUID = 1L;

	public UserDroppedException(String username) {
		super(StatusCode.ERROR, new StringBufferWrapper().append("未获取到用户：“")
														 .append(username)
														 .append("”的信息，请查看用户服务！")
														 .toString());
	}
	
}
