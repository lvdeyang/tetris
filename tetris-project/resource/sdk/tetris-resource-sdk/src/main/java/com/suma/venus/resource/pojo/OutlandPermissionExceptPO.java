package com.suma.venus.resource.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Lob;

/**
 * 外域授权例外表<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2021年3月8日 下午3:50:09
 */
@Entity
public class OutlandPermissionExceptPO extends CommonPO<OutlandPermissionExceptPO>{

	/** 外域名称路径 */
	private String serNodeNamePath;
	
	/** 目录路径 */
	private String folderPath;
	
	/** 设备id */
	private String deviceId;
	
	/** 文件夹/设备名称 */
	private String name;
	
	/** 权限类型 */
	private PermissionType permissionType;
	
	/** 资源类型 */
	private SourceType sourceType;
	
	/** 角色id */
	private Long roleId;

	@Lob
	@Column(columnDefinition = "TEXT")
	public String getSerNodeNamePath() {
		return serNodeNamePath;
	}

	public void setSerNodeNamePath(String serNodeNamePath) {
		this.serNodeNamePath = serNodeNamePath;
	}

	@Lob
	@Column(columnDefinition = "TEXT")
	public String getFolderPath() {
		return folderPath;
	}

	public void setFolderPath(String folderPath) {
		this.folderPath = folderPath;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Enumerated(value = EnumType.STRING)
	public PermissionType getPermissionType() {
		return permissionType;
	}

	public void setPermissionType(PermissionType permissionType) {
		this.permissionType = permissionType;
	}

	@Enumerated(value = EnumType.STRING)
	public SourceType getSourceType() {
		return sourceType;
	}

	public void setSourceType(SourceType sourceType) {
		this.sourceType = sourceType;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	
}