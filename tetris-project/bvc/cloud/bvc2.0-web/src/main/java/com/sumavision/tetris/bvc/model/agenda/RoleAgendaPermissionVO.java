package com.sumavision.tetris.bvc.model.agenda;

import java.util.ArrayList;
import java.util.List;

import com.sumavision.tetris.bvc.model.role.RoleChannelVO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class RoleAgendaPermissionVO extends AbstractBaseVO<RoleAgendaPermissionVO, RoleAgendaPermissionPO>{

	private Long roleId;
	
	private String roleName;
	
	private String roleUserMappingType;
	
	private Long agendaId;
	
	private List<RoleChannelVO> channels;
	
	public Long getRoleId() {
		return roleId;
	}

	public RoleAgendaPermissionVO setRoleId(Long roleId) {
		this.roleId = roleId;
		return this;
	}

	public String getRoleName() {
		return roleName;
	}

	public RoleAgendaPermissionVO setRoleName(String roleName) {
		this.roleName = roleName;
		return this;
	}

	public String getRoleUserMappingType() {
		return roleUserMappingType;
	}

	public RoleAgendaPermissionVO setRoleUserMappingType(String roleUserMappingType) {
		this.roleUserMappingType = roleUserMappingType;
		return this;
	}

	public Long getAgendaId() {
		return agendaId;
	}

	public RoleAgendaPermissionVO setAgendaId(Long agendaId) {
		this.agendaId = agendaId;
		return this;
	}

	public List<RoleChannelVO> getChannels() {
		return channels;
	}

	public RoleAgendaPermissionVO setChannels(List<RoleChannelVO> channels) {
		this.channels = channels;
		return this;
	}
	
	public RoleAgendaPermissionVO addChannel(RoleChannelVO channel) {
		if(this.channels == null) this.channels = new ArrayList<RoleChannelVO>();
		this.channels.add(channel);
		return this;
	}

	@Override
	public RoleAgendaPermissionVO set(RoleAgendaPermissionPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setRoleId(entity.getRoleId())
			.setAgendaId(entity.getAgendaId());
		return this;
	}

}
