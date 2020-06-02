package com.sumavision.tetris.bvc.model.role;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_BVC_MODEL_ROLE_CHANNEL_TERMINAL_BUNDLE_CHANNEL")
public class RoleChannelTerminalBundleChannelPermissionPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 业务角色id */
	private Long roleId;
	
	/** 业务角色通道id */
	private Long roleChannelId;
	
	/** 终端id */
	private Long terminalId;
	
	/** 终端设备id */
	private Long terminalBundleId;
	
	/** 终端设备通道id */
	private Long terminalBundleChannelId;

	@Column(name = "ROLE_ID")
	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	@Column(name = "ROLE_CHANNEL_ID")
	public Long getRoleChannelId() {
		return roleChannelId;
	}

	public void setRoleChannelId(Long roleChannelId) {
		this.roleChannelId = roleChannelId;
	}

	@Column(name = "TERMINAL_ID")
	public Long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
	}

	@Column(name = "TERMINAL_BUNDLE_ID")
	public Long getTerminalBundleId() {
		return terminalBundleId;
	}

	public void setTerminalBundleId(Long terminalBundleId) {
		this.terminalBundleId = terminalBundleId;
	}

	@Column(name = "TERMINAL_BUNDLE_CHANNEL_ID")
	public Long getTerminalBundleChannelId() {
		return terminalBundleChannelId;
	}

	public void setTerminalBundleChannelId(Long terminalBundleChannelId) {
		this.terminalBundleChannelId = terminalBundleChannelId;
	}
	
}
