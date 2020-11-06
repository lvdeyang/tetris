package com.sumavision.tetris.user;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_USER_TAGS")
public class UserTagsPO extends AbstractBasePO{
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
