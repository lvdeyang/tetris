package com.sumavision.tetris.system.role;

public class UserSystemRolePermissionVO {

	private Long id;
	
	private String uuid;
	
	private String updateTime;
	
	private Long roleId;
	
	private String roleName;
	
	private Long userId;
	
	private String username;
	
	private String nickname;
	
	private boolean autoGeneration;

	public Long getId() {
		return id;
	}

	public UserSystemRolePermissionVO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public UserSystemRolePermissionVO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public UserSystemRolePermissionVO setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public Long getRoleId() {
		return roleId;
	}

	public UserSystemRolePermissionVO setRoleId(Long roleId) {
		this.roleId = roleId;
		return this;
	}

	public String getRoleName() {
		return roleName;
	}

	public UserSystemRolePermissionVO setRoleName(String roleName) {
		this.roleName = roleName;
		return this;
	}

	public Long getUserId() {
		return userId;
	}

	public UserSystemRolePermissionVO setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	public String getUsername() {
		return username;
	}

	public UserSystemRolePermissionVO setUsername(String username) {
		this.username = username;
		return this;
	}

	public String getNickname() {
		return nickname;
	}

	public UserSystemRolePermissionVO setNickname(String nickname) {
		this.nickname = nickname;
		return this;
	}

	public boolean isAutoGeneration() {
		return autoGeneration;
	}

	public UserSystemRolePermissionVO setAutoGeneration(boolean autoGeneration) {
		this.autoGeneration = autoGeneration;
		return this;
	}
}
