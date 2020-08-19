package com.suma.venus.resource.base.bo;

public class CheckUserResultBO extends ResultBO{

	private Long userId;

	private String extraInfo;
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}
	
}
