package com.suma.venus.resource.base.bo;

import java.util.ArrayList;
import java.util.List;

public class UnbindUserPrivilegeBO {
	
	private Long userId;
	
	private List<UnbindResouceBO> unbindPrivilege = new ArrayList<UnbindResouceBO>();

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<UnbindResouceBO> getUnbindPrivilege() {
		return unbindPrivilege;
	}

	public void setUnbindPrivilege(List<UnbindResouceBO> unbindPrivilege) {
		this.unbindPrivilege = unbindPrivilege;
	}
	
}
