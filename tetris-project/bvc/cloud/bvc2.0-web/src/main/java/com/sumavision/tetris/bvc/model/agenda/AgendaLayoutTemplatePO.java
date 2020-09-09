package com.sumavision.tetris.bvc.model.agenda;

import javax.jdo.annotations.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 执行议程时改变终端布局<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月4日 下午3:27:56
 */
@Entity
@Table(name = "TETRIS_BVC_MODEL_AGENDA_LAYOUT_TEMPLATE")
public class AgendaLayoutTemplatePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 角色id */
	private Long roleId;
	
	/** 终端id */
	private Long terminalId;
	
	/** 布局id */
	private Long layoutId;
	
	/** 议程id */
	private Long agendaId;

	@Column(name = "ROLE_ID")
	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	@Column(name = "TERMINAL_ID")
	public Long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
	}

	@Column(name = "LAYOUT_ID")
	public Long getLayoutId() {
		return layoutId;
	}

	public void setLayoutId(Long layoutId) {
		this.layoutId = layoutId;
	}

	@Column(name = "AGENDA_ID")
	public Long getAgendaId() {
		return agendaId;
	}

	public void setAgendaId(Long agendaId) {
		this.agendaId = agendaId;
	}
	
}
