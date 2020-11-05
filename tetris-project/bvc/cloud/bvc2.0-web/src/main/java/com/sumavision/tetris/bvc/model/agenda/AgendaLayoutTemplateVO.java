package com.sumavision.tetris.bvc.model.agenda;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class AgendaLayoutTemplateVO extends AbstractBaseVO<AgendaLayoutTemplateVO, AgendaLayoutTemplatePO>{

	private Long roleId;
	
	private String roleName;
	
	private Long terminalId;
	
	private String terminalName;
	
	private Long layoutId;
	
	private String layoutName;
	
	private Long agendaId;
	
	private String agendaName;
	
	public Long getRoleId() {
		return roleId;
	}

	public AgendaLayoutTemplateVO setRoleId(Long roleId) {
		this.roleId = roleId;
		return this;
	}

	public String getRoleName() {
		return roleName;
	}

	public AgendaLayoutTemplateVO setRoleName(String roleName) {
		this.roleName = roleName;
		return this;
	}

	public Long getTerminalId() {
		return terminalId;
	}

	public AgendaLayoutTemplateVO setTerminalId(Long terminalId) {
		this.terminalId = terminalId;
		return this;
	}

	public String getTerminalName() {
		return terminalName;
	}

	public AgendaLayoutTemplateVO setTerminalName(String terminalName) {
		this.terminalName = terminalName;
		return this;
	}

	public Long getLayoutId() {
		return layoutId;
	}

	public AgendaLayoutTemplateVO setLayoutId(Long layoutId) {
		this.layoutId = layoutId;
		return this;
	}

	public String getLayoutName() {
		return layoutName;
	}

	public AgendaLayoutTemplateVO setLayoutName(String layoutName) {
		this.layoutName = layoutName;
		return this;
	}

	public Long getAgendaId() {
		return agendaId;
	}

	public AgendaLayoutTemplateVO setAgendaId(Long agendaId) {
		this.agendaId = agendaId;
		return this;
	}

	public String getAgendaName() {
		return agendaName;
	}

	public AgendaLayoutTemplateVO setAgendaName(String agendaName) {
		this.agendaName = agendaName;
		return this;
	}

	@Override
	public AgendaLayoutTemplateVO set(AgendaLayoutTemplatePO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setRoleId(entity.getRoleId())
			.setTerminalId(entity.getTerminalId())
			.setLayoutId(entity.getLayoutId())
			.setAgendaId(entity.getAgendaId());
		return this;
	}

}
