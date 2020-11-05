package com.sumavision.bvc.device.group.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

/**
 * 单个会议的总人数超出限制
 * @author zsy
 *
 */
public class AllRunningMeetingsNumberOfMembersOverLimitException extends BaseException{

	private static final long serialVersionUID = 1L;
	

	public AllRunningMeetingsNumberOfMembersOverLimitException(int allRunningMeetingsNumberOfMembersLimit){
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("进行中的所有会议总人数不能超过：")
															 .append(allRunningMeetingsNumberOfMembersLimit)
															 .append("人")
															 .toString());
	}
	
}
