package com.sumavision.bvc.device.group.dto;

import java.util.Date;

public class DeviceGroupMemberDTO {

	private Long id;
	
	private String uuid;
	
	private Date updateTime;
	
	private String bundleId;

	private String bundleName;
	
	/**对应BundlePO的deviceModel，如jv210*/
	private String bundleType;
	
	/**对应BundlePO的bundleType，如VenusTerminal*/
	private String venusBundleType;

	private String layerId;

	private Long folderId;

	private Long roleId;
	
	private String roleName;
	
	private boolean openAudio;
	
	public DeviceGroupMemberDTO(){}
	
	public DeviceGroupMemberDTO(
			Long id, 
			String uuid, 
			Date updateTime, 
			String bundleId, 
			String bundleName, 
			String bundleType,
			String venusBundleType,
			String layerId,
			Long folderId, 
			Long roleId, 
			String roleName,
			boolean openAudio) {
		
		this.id = id;
		this.uuid = uuid;
		this.updateTime = updateTime;
		this.bundleId = bundleId;
		this.bundleName = bundleName;
		this.bundleType = bundleType;
		this.venusBundleType = venusBundleType;
		this.layerId = layerId;
		this.folderId = folderId;
		this.roleId = roleId;
		this.roleName = roleName;
		this.openAudio = openAudio;
	}

	public Long getId() {
		return id;
	}

	public DeviceGroupMemberDTO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public DeviceGroupMemberDTO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public DeviceGroupMemberDTO setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public String getBundleId() {
		return bundleId;
	}

	public DeviceGroupMemberDTO setBundleId(String bundleId) {
		this.bundleId = bundleId;
		return this;
	}

	public String getBundleName() {
		return bundleName;
	}

	public DeviceGroupMemberDTO setBundleName(String bundleName) {
		this.bundleName = bundleName;
		return this;
	}

	public String getBundleType() {
		return bundleType;
	}

	public DeviceGroupMemberDTO setBundleType(String bundleType) {
		this.bundleType = bundleType;
		return this;
	}

	public String getVenusBundleType() {
		return venusBundleType;
	}

	public DeviceGroupMemberDTO setVenusBundleType(String venusBundleType) {
		this.venusBundleType = venusBundleType;
		return this;
	}

	public String getLayerId() {
		return layerId;
	}

	public DeviceGroupMemberDTO setLayerId(String layerId) {
		this.layerId = layerId;
		return this;
	}

	public Long getFolderId() {
		return folderId;
	}

	public DeviceGroupMemberDTO setFolderId(Long folderId) {
		this.folderId = folderId;
		return this;
	}

	public Long getRoleId() {
		return roleId;
	}

	public DeviceGroupMemberDTO setRoleId(Long roleId) {
		this.roleId = roleId;
		return this;
	}

	public String getRoleName() {
		return roleName;
	}

	public DeviceGroupMemberDTO setRoleName(String roleName) {
		this.roleName = roleName;
		return this;
	}

	public boolean isOpenAudio() {
		return openAudio;
	}

	public DeviceGroupMemberDTO setOpenAudio(boolean openAudio) {
		this.openAudio = openAudio;
		return this;
	}
}
