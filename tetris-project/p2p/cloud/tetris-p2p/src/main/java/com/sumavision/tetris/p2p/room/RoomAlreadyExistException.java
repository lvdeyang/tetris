package com.sumavision.tetris.p2p.room;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class RoomAlreadyExistException extends BaseException{

	private static final long serialVersionUID = 1L;

	public RoomAlreadyExistException(String roomId) {
		super(StatusCode.CONFLICT, new StringBufferWrapper().append("该房间正在使用，房间号为：")
														 	.append(roomId)
														 	.toString());
	}

}
