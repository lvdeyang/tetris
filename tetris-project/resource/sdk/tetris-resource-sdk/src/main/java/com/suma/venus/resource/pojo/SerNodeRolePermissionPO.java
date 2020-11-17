package com.suma.venus.resource.pojo;

import javax.persistence.Entity;

@Entity
public class SerNodeRolePermissionPO extends CommonPO<SerNodeRolePermissionPO>{
	
	private Long roleId;
	
	private Long serNodeId;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getSerNodeId() {
		return serNodeId;
	}

	public void setSerNodeId(Long serNodeId) {
		this.serNodeId = serNodeId;
	}

}
