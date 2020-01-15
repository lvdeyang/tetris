package com.suma.venus.resource.base.bo;

import java.util.List;

public class UnbindRolePrivilegeBO {

	private Long roleId;
	
	private List<UnbindResouceBO> unbindPrivilege;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public List<UnbindResouceBO> getUnbindPrivilege() {
		return unbindPrivilege;
	}

	public void setUnbindPrivilege(List<UnbindResouceBO> unbindPrivilege) {
		this.unbindPrivilege = unbindPrivilege;
	}
	
}
