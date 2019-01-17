package com.sumavision.tetris.mims.app.organization;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "MIMS_ORGANIZATION_USER_PERMISSION")
public class OrganizationUserPermissionPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 关联用户id */
	private String userId;
	
	private Long organizationId;

	@Column(name = "USER_ID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "ORGANIZATION_ID")
	public Long getOrganizationId() {
		return organizationId;
	}

	public void setOrganizationId(Long organizationId) {
		this.organizationId = organizationId;
	}
	
}
