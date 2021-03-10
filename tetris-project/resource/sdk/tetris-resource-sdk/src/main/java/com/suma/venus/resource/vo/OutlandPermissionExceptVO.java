package com.suma.venus.resource.vo;

import com.suma.venus.resource.pojo.OutlandPermissionExceptPO;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

/**
 * 外域授权例外表<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年3月8日 下午3:50:09
 */
public class OutlandPermissionExceptVO extends AbstractBaseVO<OutlandPermissionExceptVO, OutlandPermissionExceptPO>{

	private String serNodeNamePath;
	
	private String folderPath;
	
	private String deviceId;
	
	private String permissionType;
	
	private String permissionTypeName;
	
	private String sourceType;
	
	private String sourceTypeName;
	
	private Long roleId;
	
	public String getSerNodeNamePath() {
		return serNodeNamePath;
	}

	public OutlandPermissionExceptVO setSerNodeNamePath(String serNodeNamePath) {
		this.serNodeNamePath = serNodeNamePath;
		return this;
	}

	public String getFolderPath() {
		return folderPath;
	}

	public OutlandPermissionExceptVO setFolderPath(String folderPath) {
		this.folderPath = folderPath;
		return this;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public OutlandPermissionExceptVO setDeviceId(String deviceId) {
		this.deviceId = deviceId;
		return this;
	}

	public String getPermissionType() {
		return permissionType;
	}

	public OutlandPermissionExceptVO setPermissionType(String permissionType) {
		this.permissionType = permissionType;
		return this;
	}

	public String getPermissionTypeName() {
		return permissionTypeName;
	}

	public OutlandPermissionExceptVO setPermissionTypeName(String permissionTypeName) {
		this.permissionTypeName = permissionTypeName;
		return this;
	}

	public String getSourceType() {
		return sourceType;
	}

	public OutlandPermissionExceptVO setSourceType(String sourceType) {
		this.sourceType = sourceType;
		return this;
	}

	public String getSourceTypeName() {
		return sourceTypeName;
	}

	public OutlandPermissionExceptVO setSourceTypeName(String sourceTypeName) {
		this.sourceTypeName = sourceTypeName;
		return this;
	}

	public Long getRoleId() {
		return roleId;
	}

	public OutlandPermissionExceptVO setRoleId(Long roleId) {
		this.roleId = roleId;
		return this;
	}

	@Override
	public OutlandPermissionExceptVO set(OutlandPermissionExceptPO entity) throws Exception {
		this.setId(entity.getId())
			.setSerNodeNamePath(entity.getSerNodeNamePath())
			.setFolderPath(entity.getFolderPath())
			.setDeviceId(entity.getDeviceId())
			.setPermissionType(entity.getPermissionType().toString())
			.setPermissionType(entity.getPermissionType().getName())
			.setSourceType(entity.getSourceType().toString())
			.setSourceType(entity.getSourceType().getName())
			.setRoleId(entity.getRoleId());
		return this;
	}
	
}
