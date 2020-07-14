package com.sumavision.tetris.bvc.model.role;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class RoleChannelVO extends AbstractBaseVO<RoleChannelVO, RoleChannelPO>{

	private String name;
	
	private String type;
	
	private String typeName;
	
	private Long roleId;

	public String getName() {
		return name;
	}

	public RoleChannelVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getType() {
		return type;
	}

	public RoleChannelVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getTypeName() {
		return typeName;
	}

	public RoleChannelVO setTypeName(String typeName) {
		this.typeName = typeName;
		return this;
	}

	public Long getRoleId() {
		return roleId;
	}

	public RoleChannelVO setRoleId(Long roleId) {
		this.roleId = roleId;
		return this;
	}

	@Override
	public RoleChannelVO set(RoleChannelPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setType(entity.getType().toString())
			.setTypeName(entity.getType().getName())
			.setRoleId(entity.getRoleId());
		return this;
	}
	
}
