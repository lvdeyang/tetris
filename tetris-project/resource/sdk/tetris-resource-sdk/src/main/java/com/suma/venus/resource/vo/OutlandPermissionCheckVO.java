package com.suma.venus.resource.vo;

import com.suma.venus.resource.pojo.OutlandPermissionCheckPO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class OutlandPermissionCheckVO extends AbstractBaseVO<OutlandPermissionCheckVO, OutlandPermissionCheckPO>{

	private String serNodeNamePath;
	
	private String folderPath;
	
	private String deviceId;
	
	private String name;
	
	private String permissionType;
	
	private String permissionTypeName;
	
	private String sourceType;
	
	private String sourceTypeName;
	
	private Long roleId;

	public String getSerNodeNamePath() {
		return serNodeNamePath;
	}

	public OutlandPermissionCheckVO setSerNodeNamePath(String serNodeNamePath) {
		this.serNodeNamePath = serNodeNamePath;
		return this;
	}

	public String getFolderPath() {
		return folderPath;
	}

	public OutlandPermissionCheckVO setFolderPath(String folderPath) {
		this.folderPath = folderPath;
		return this;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public OutlandPermissionCheckVO setDeviceId(String deviceId) {
		this.deviceId = deviceId;
		return this;
	}

	public String getName() {
		return name;
	}

	public OutlandPermissionCheckVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getPermissionType() {
		return permissionType;
	}

	public OutlandPermissionCheckVO setPermissionType(String permissionType) {
		this.permissionType = permissionType;
		return this;
	}

	public String getPermissionTypeName() {
		return permissionTypeName;
	}

	public OutlandPermissionCheckVO setPermissionTypeName(String permissionTypeName) {
		this.permissionTypeName = permissionTypeName;
		return this;
	}

	public String getSourceType() {
		return sourceType;
	}

	public OutlandPermissionCheckVO setSourceType(String sourceType) {
		this.sourceType = sourceType;
		return this;
	}

	public String getSourceTypeName() {
		return sourceTypeName;
	}

	public OutlandPermissionCheckVO setSourceTypeName(String sourceTypeName) {
		this.sourceTypeName = sourceTypeName;
		return this;
	}

	public Long getRoleId() {
		return roleId;
	}

	public OutlandPermissionCheckVO setRoleId(Long roleId) {
		this.roleId = roleId;
		return this;
	}

	@Override
	public OutlandPermissionCheckVO set(OutlandPermissionCheckPO entity) throws Exception {
		this.setId(entity.getId())
			.setSerNodeNamePath(entity.getSerNodeNamePath())
			.setFolderPath(entity.getFolderPath())
			.setDeviceId(entity.getDeviceId())
			.setName(entity.getName())
			.setPermissionType(entity.getPermissionType().toString())
			.setPermissionTypeName(entity.getPermissionType().getName())
			.setSourceType(entity.getSourceType().toString())
			.setSourceTypeName(entity.getSourceType().getName())
			.setRoleId(entity.getRoleId());
		return this;
	}
	
}
