package com.sumavision.bvc.device.group.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

/**
 * 进行中的所有会议的总人数超出限制
 * @author zsy
 *
 */
public class SingleMeetingNumberOfMembersOverLimitException extends BaseException{

	private static final long serialVersionUID = 1L;
	
	public SingleMeetingNumberOfMembersOverLimitException(int singleMeetingNumberOfMembersLimit){
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("会议人数不能超过：")
															 .append(singleMeetingNumberOfMembersLimit)
															 .append("人")
															 .toString());
	}
	
}
