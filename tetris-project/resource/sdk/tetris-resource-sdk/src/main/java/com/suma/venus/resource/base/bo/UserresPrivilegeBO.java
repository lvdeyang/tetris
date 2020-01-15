package com.suma.venus.resource.base.bo;

public class UserresPrivilegeBO {

	private Long id;
	
	private String name;
	
	private String userNo;
	
	//(录制权限)
	private boolean hasReadPrivilege = false;
	
	//点播权限
	private boolean hasWritePrivilege = false;
	
	//呼叫权限
	private boolean hasHJPrivilege = false;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public boolean isHasReadPrivilege() {
		return hasReadPrivilege;
	}

	public void setHasReadPrivilege(boolean hasReadPrivilege) {
		this.hasReadPrivilege = hasReadPrivilege;
	}

	public boolean isHasWritePrivilege() {
		return hasWritePrivilege;
	}

	public void setHasWritePrivilege(boolean hasWritePrivilege) {
		this.hasWritePrivilege = hasWritePrivilege;
	}

	public boolean isHasHJPrivilege() {
		return hasHJPrivilege;
	}

	public void setHasHJPrivilege(boolean hasHJPrivilege) {
		this.hasHJPrivilege = hasHJPrivilege;
	}
	
}
