package com.suma.venus.resource.base.bo;

public class BundlePrivilegeBO {
	
	private Long id;
	
	private String name;

	private String bundleId;
	
	private String deviceModel;
	
	private boolean hasReadPrivilege = false;
	
	private boolean hasWritePrivilege = false;
	
	private boolean hasCloudPrivilege = false;
	
	private boolean hasDownloadPrivilege = false;
	
	private boolean hasLocalReadPrivilege = false;
	
	private String username;
	
	private String codec;
	
	private boolean canReadPrivilege = false;
	
	private boolean canWritePrivilege = false;
	
	private boolean canCloudPrivilege = false;
	
	private boolean canDownloadPrivilege = false;
	
	private boolean canLocalReadPrivilege = false;
	
	public boolean isCanReadPrivilege() {
		return canReadPrivilege;
	}

	public void setCanReadPrivilege(boolean canReadPrivilege) {
		this.canReadPrivilege = canReadPrivilege;
	}

	public boolean isCanWritePrivilege() {
		return canWritePrivilege;
	}

	public void setCanWritePrivilege(boolean canWritePrivilege) {
		this.canWritePrivilege = canWritePrivilege;
	}

	public boolean isCanCloudPrivilege() {
		return canCloudPrivilege;
	}

	public void setCanCloudPrivilege(boolean canCloudPrivilege) {
		this.canCloudPrivilege = canCloudPrivilege;
	}

	public boolean isCanDownloadPrivilege() {
		return canDownloadPrivilege;
	}

	public void setCanDownloadPrivilege(boolean canDownloadPrivilege) {
		this.canDownloadPrivilege = canDownloadPrivilege;
	}

	public boolean isCanLocalReadPrivilege() {
		return canLocalReadPrivilege;
	}

	public void setCanLocalReadPrivilege(boolean canLocalReadPrivilege) {
		this.canLocalReadPrivilege = canLocalReadPrivilege;
	}

	public String getCodec() {
		return codec;
	}

	public void setCodec(String codec) {
		this.codec = codec;
	}

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

	public boolean isHasCloudPrivilege() {
		return hasCloudPrivilege;
	}

	public void setHasCloudPrivilege(boolean hasCloudPrivilege) {
		this.hasCloudPrivilege = hasCloudPrivilege;
	}

	public boolean isHasDownloadPrivilege() {
		return hasDownloadPrivilege;
	}

	public void setHasDownloadPrivilege(boolean hasDownloadPrivilege) {
		this.hasDownloadPrivilege = hasDownloadPrivilege;
	}

	public boolean isHasLocalReadPrivilege() {
		return hasLocalReadPrivilege;
	}

	public void setHasLocalReadPrivilege(boolean hasLocalReadPrivilege) {
		this.hasLocalReadPrivilege = hasLocalReadPrivilege;
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
