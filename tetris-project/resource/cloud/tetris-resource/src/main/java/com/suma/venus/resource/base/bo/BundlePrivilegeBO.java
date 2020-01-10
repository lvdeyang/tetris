package com.suma.venus.resource.base.bo;

public class BundlePrivilegeBO {
	
	private Long id;
	
	private String name;

	private String bundleId;
	
	private String deviceModel;
	
	private boolean hasReadPrivilege = false;
	
	private boolean hasWritePrivilege = false;
	
	private String username;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
