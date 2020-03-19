package com.sumavision.tetris.zoom.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class ZoomMemberNotFoundException extends BaseException{

	private static final long serialVersionUID = 1L;

	public ZoomMemberNotFoundException(Long zoomMemberId) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("会议成员不存在！id：").append(zoomMemberId).toString());
		
	}
	
	public ZoomMemberNotFoundException(String zoomMemberIds) {
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("会议成员不存在！id：").append(zoomMemberIds).toString());
		
	}

}
