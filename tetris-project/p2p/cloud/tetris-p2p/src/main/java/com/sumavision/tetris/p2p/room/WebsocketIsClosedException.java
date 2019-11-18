package com.sumavision.tetris.p2p.room;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class WebsocketIsClosedException extends BaseException{
	
	private static final long serialVersionUID = 1L;

	public WebsocketIsClosedException(String key) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("用户与服务器websocket未连接，连接标识为：")
															 .append(key)
															 .toString());
	}

}
