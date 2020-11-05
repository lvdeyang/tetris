package com.sumavision.tetris.p2p.room;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class UserAlreadyInRoomException extends BaseException{

	private static final long serialVersionUID = 1L;

	public UserAlreadyInRoomException(Long userId, String userName) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("用户正在点对点通话，用户id为：")
														     .append(userId)
														     .append("，用户名为：")
														     .append(userName)
														     .toString());
	}

}
