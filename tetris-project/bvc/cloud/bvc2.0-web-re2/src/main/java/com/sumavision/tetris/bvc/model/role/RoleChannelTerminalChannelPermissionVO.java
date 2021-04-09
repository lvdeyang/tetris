package com.sumavision.tetris.bvc.model.role;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class RoleChannelTerminalChannelPermissionVO extends AbstractBaseVO<RoleChannelTerminalChannelPermissionVO, RoleChannelTerminalChannelPermissionPO>{

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

	public RoleChannelTerminalChannelPermissionVO setRoleId(Long roleId) {
		this.roleId = roleId;
		return this;
	}

	public String getRoleName() {
		return roleName;
	}

	public RoleChannelTerminalChannelPermissionVO setRoleName(String roleName) {
		this.roleName = roleName;
		return this;
	}

	public Long getRoleChannelId() {
		return roleChannelId;
	}

	public RoleChannelTerminalChannelPermissionVO setRoleChannelId(Long roleChannelId) {
		this.roleChannelId = roleChannelId;
		return this;
	}

	public String getRoleChannelName() {
		return roleChannelName;
	}

	public RoleChannelTerminalChannelPermissionVO setRoleChannelName(String roleChannelName) {
		this.roleChannelName = roleChannelName;
		return this;
	}

	public Long getTerminalId() {
		return terminalId;
	}

	public RoleChannelTerminalChannelPermissionVO setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
		return this;
	}

	public String getTerminalName() {
		return terminalName;
	}

	public RoleChannelTerminalChannelPermissionVO setTerminalName(String terminalName) {
		this.terminalName = terminalName;
		return this;
	}

	public Long getTerminalChannelId() {
		return terminalChannelId;
	}

	public RoleChannelTerminalChannelPermissionVO setTerminalChannelId(Long terminalChannelId) {
		this.terminalChannelId = terminalChannelId;
		return this;
	}

	public String getTerminalChannelName() {
		return terminalChannelName;
	}

	public RoleChannelTerminalChannelPermissionVO setTerminalChannelName(String terminalChannelName) {
		this.terminalChannelName = terminalChannelName;
		return this;
	}

	@Override
	public RoleChannelTerminalChannelPermissionVO set(RoleChannelTerminalChannelPermissionPO entity) throws Exception {
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
