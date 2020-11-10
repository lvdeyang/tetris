package com.sumavision.tetris.user;

import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class UserTagsVO {
	private Long userId;
	private String tagName;
	private Long hotCount;
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}

	public Long getHotCount() {
		return hotCount;
	}

	public void setHotCount(Long hotCount) {
		this.hotCount = hotCount;
	}

	
}
