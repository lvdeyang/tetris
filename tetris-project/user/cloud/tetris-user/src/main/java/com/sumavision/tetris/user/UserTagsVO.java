package com.sumavision.tetris.user;

import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class UserTagsVO extends AbstractBaseVO<UserTagsVO, UserTagsPO>{
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

	@Override
	public UserTagsVO set(UserTagsPO entity) throws Exception {
		// TODO Auto-generated method stub
		this.setHotCount(entity.getHotCount()==null?0:entity.getHotCount());
		this.setTagName(entity.getTagName());
		this.setUserId(entity.getUserId());
		return this;
	}

}
