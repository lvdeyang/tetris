package com.sumavision.tetris.bvc.model.agenda.role;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_BVC_MODEL_AGENDA_ROLE_COLLECTION_ITEM")
public class RoleCollectionItemPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 业务角色id */
	private Long roleId;
	
	/** 业务角色通道id */
	private Long roleChannelId;

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
	
}
