package com.sumavision.tetris.cms.column;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_CMS_COLUMN_USER_PERMISSION")
public class ColumnUserPermissionPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	private Long columnId;
	
	private String userId;
	
	private String groupId;

	@Column(name = "COLUMN_ID")
	public Long getColumnId() {
		return columnId;
	}

	public void setColumnId(Long columnId) {
		this.columnId = columnId;
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
