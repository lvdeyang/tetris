package com.sumavision.tetris.mims.app.media.tag;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_TAG_GROUP_PERMISSION")
public class TagGroupPermissionPO extends AbstractBasePO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** 标签id */
	private Long tagId;
	
	/** 组织id */
	private String groupId;

	@Column(name = "TAG_ID")
	public Long getTagId() {
		return tagId;
	}

	public void setTagId(Long tagId) {
		this.tagId = tagId;
	}

	@Column(name = "GROUP_ID")
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
}
