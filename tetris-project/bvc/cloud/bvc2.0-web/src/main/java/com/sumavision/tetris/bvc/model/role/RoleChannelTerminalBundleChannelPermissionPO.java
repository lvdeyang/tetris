package com.sumavision.tetris.bvc.model.role;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 角色通道与终端通道关联<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月4日 下午3:24:45
 */
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
	
	/** 终端通道id */
	private Long terminalChannelId;
	
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

	@Column(name = "TERMINAL_CHANNEL_ID")
	public Long getTerminalChannelId() {
		return terminalChannelId;
	}

	public void setTerminalChannelId(Long terminalChannelId) {
		this.terminalChannelId = terminalChannelId;
	}
	
}
