package com.sumavision.tetris.p2p.room;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class RoomUserIsCorrectException extends BaseException{
	
	private static final long serialVersionUID = 1L;

	public RoomUserIsCorrectException(String roomId, Long roomUserId1, Long roomUserId2, Long userId1, Long userId2) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("房间成员不正确，房间号为：")
															 .append(roomId)
															 .append("，房间成员为：")
															 .append(roomUserId1)
															 .append("和")
															 .append(roomUserId2)
															 .append(",请求成员为：")
															 .append(userId1)
															 .append("和")
															 .append(userId2)
															 .toString());
	}
	
}
