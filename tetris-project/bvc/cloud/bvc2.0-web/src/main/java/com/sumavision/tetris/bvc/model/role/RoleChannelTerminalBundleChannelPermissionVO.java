package com.sumavision.tetris.bvc.model.role;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class RoleChannelTerminalBundleChannelPermissionVO extends AbstractBaseVO<RoleChannelTerminalBundleChannelPermissionVO, RoleChannelTerminalBundleChannelPermissionPO>{

	private Long roleId;
	
	private String roleName;
	
	private Long roleChannelId;
	
	private String roleChannelName;
	
	private Long terminalId;
	
	private String terminalName;
	
	private Long terminalChannelId;
	
	private String terminalChannelName;
	
	public Long getRoleId() {
		return roleId;
	}

	public RoleChannelTerminalBundleChannelPermissionVO setRoleId(Long roleId) {
		this.roleId = roleId;
		return this;
	}

	public String getRoleName() {
		return roleName;
	}

	public RoleChannelTerminalBundleChannelPermissionVO setRoleName(String roleName) {
		this.roleName = roleName;
		return this;
	}

	public Long getRoleChannelId() {
		return roleChannelId;
	}

	public RoleChannelTerminalBundleChannelPermissionVO setRoleChannelId(Long roleChannelId) {
		this.roleChannelId = roleChannelId;
		return this;
	}

	public String getRoleChannelName() {
		return roleChannelName;
	}

	public RoleChannelTerminalBundleChannelPermissionVO setRoleChannelName(String roleChannelName) {
		this.roleChannelName = roleChannelName;
		return this;
	}

	public Long getTerminalId() {
		return terminalId;
	}

	public RoleChannelTerminalBundleChannelPermissionVO setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
		return this;
	}

	public String getTerminalName() {
		return terminalName;
	}

	public RoleChannelTerminalBundleChannelPermissionVO setTerminalName(String terminalName) {
		this.terminalName = terminalName;
		return this;
	}

	public Long getTerminalChannelId() {
		return terminalChannelId;
	}

	public RoleChannelTerminalBundleChannelPermissionVO setTerminalChannelId(Long terminalChannelId) {
		this.terminalChannelId = terminalChannelId;
		return this;
	}

	public String getTerminalChannelName() {
		return terminalChannelName;
	}

	public RoleChannelTerminalBundleChannelPermissionVO setTerminalChannelName(String terminalChannelName) {
		this.terminalChannelName = terminalChannelName;
		return this;
	}

	@Override
	public RoleChannelTerminalBundleChannelPermissionVO set(RoleChannelTerminalBundleChannelPermissionPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setRoleId(entity.getRoleId())
			.setRoleChannelId(entity.getRoleChannelId())
			.setTerminalId(entity.getTerminalId())
			.setTerminalChannelId(entity.getTerminalChannelId());
		return this;
	}
	
}
