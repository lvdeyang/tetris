package com.suma.venus.resource.base.bo;

import java.util.List;

public class UserPrivilegeBO {

	private List<ResourcePrivilegeBO> privilegePermission;

	public List<ResourcePrivilegeBO> getPrivilegePermission() {
		return privilegePermission;
	}

	public void setPrivilegePermission(List<ResourcePrivilegeBO> privilegePermission) {
		this.privilegePermission = privilegePermission;
	}
	
}
