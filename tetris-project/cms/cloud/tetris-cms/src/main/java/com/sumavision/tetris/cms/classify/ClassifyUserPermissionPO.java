package com.sumavision.tetris.cms.classify;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_CMS_CLASSIFY_USER_PERMISSION")
public class ClassifyUserPermissionPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;
	
	private Long classifyId;
	
	private String userId;
	
	private String groupId;

	@Column(name = "CLASSIFY_ID")
	public Long getClassifyId() {
		return classifyId;
	}

	public void setClassifyId(Long classifyId) {
		this.classifyId = classifyId;
	}

	@Column(name = "USER_ID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "GROUP_ID")
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
}
