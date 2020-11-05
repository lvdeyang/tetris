package com.suma.venus.resource.base.bo;

import java.util.List;

public class RoleAndResourceIdBO {

	private Long roleId;
	
	private List<String> resourceCodes;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public List<String> getResourceCodes() {
		return resourceCodes;
	}

	public void setResourceCodes(List<String> resourceCodes) {
		this.resourceCodes = resourceCodes;
	}
	
}
