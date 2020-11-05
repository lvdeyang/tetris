package com.sumavision.bvc.device.command.exception;

import com.sumavision.tetris.commons.exception.BaseException;
import com.sumavision.tetris.commons.exception.code.StatusCode;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;

public class CommandGroupNameAlreadyExistedException extends BaseException {

	public static final long serialVersionUID = 1L;
	
	//协议码
	private String recommendedName;
	
	public CommandGroupNameAlreadyExistedException(String groupName){
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("名称 ")
															 .append(groupName)
															 .append(" 已被使用，请修改")
															 .toString());
	}
	
	public CommandGroupNameAlreadyExistedException(String groupName, String recommendedName){
		super(StatusCode.FORBIDDEN, new StringBufferWrapper().append("名称 ")
															 .append(groupName)
															 .append(" 已被使用，请修改")
															 .toString());
		this.setRecommendedName(recommendedName);
	}

	public String getRecommendedName() {
		return recommendedName;
	}

	public void setRecommendedName(String recommendedName) {
		this.recommendedName = recommendedName;
	}
	
}