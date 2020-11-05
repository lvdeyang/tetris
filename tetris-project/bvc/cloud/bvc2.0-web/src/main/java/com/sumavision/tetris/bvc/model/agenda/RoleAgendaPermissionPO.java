package com.sumavision.tetris.bvc.model.agenda;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 议程角色关联<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月4日 下午3:26:43
 */
@Entity
@Table(name = "TETRIS_BVC_MODEL_ROLE_AGENDA_PERMISSION")
public class RoleAgendaPermissionPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 业务角色id */
	private Long roleId;
	
	/** 议程id */
	private Long agendaId;

	@Column(name = "ROLE_ID")
	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	@Column(name = "AGENDA_ID")
	public Long getAgendaId() {
		return agendaId;
	}

	public void setAgendaId(Long agendaId) {
		this.agendaId = agendaId;
	}
	
}
