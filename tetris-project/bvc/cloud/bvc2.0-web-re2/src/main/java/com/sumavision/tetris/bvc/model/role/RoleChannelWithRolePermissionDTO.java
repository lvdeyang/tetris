package com.sumavision.tetris.bvc.model.role;

public class RoleChannelWithRolePermissionDTO {

	/** 角色通道id */
	private Long id;
	
	/** 通道名称 */
	private String name;
	
	private RoleChannelType channelType;
	
	private Long roleId;
	
	private String roleName;
	
	private InternalRoleType internalRoleType;

	public RoleChannelWithRolePermissionDTO(
			Long id,
			String name,
			RoleChannelType channelType,
			Long roleId,
			String roleName,
			InternalRoleType internalRoleType){
		this.id = id;
		this.name = name;
		this.channelType = channelType;
		this.roleId = roleId;
		this.roleName = roleName;
		this.internalRoleType = internalRoleType;
	}
	
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

	public RoleChannelType getChannelType() {
		return channelType;
	}

	public void setChannelType(RoleChannelType channelType) {
		this.channelType = channelType;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public InternalRoleType getInternalRoleType() {
		return internalRoleType;
	}

	public void setInternalRoleType(InternalRoleType internalRoleType) {
		this.internalRoleType = internalRoleType;
	}
	
}
