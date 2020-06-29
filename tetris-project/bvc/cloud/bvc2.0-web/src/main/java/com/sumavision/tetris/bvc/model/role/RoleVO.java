package com.sumavision.tetris.bvc.model.role;

import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class RoleVO extends AbstractBaseVO<RoleVO, RolePO>{

	private String name;
	
	private String internalRoleType;
	
	private String internalRoleTypeName;
	
	private String roleUserMappingType;
	
	private String roleUserMappingTypeName;
	
	public String getName() {
		return name;
	}

	public RoleVO setName(String name) {
		this.name = name;
		return this;
	}

	public String getInternalRoleType() {
		return internalRoleType;
	}

	public RoleVO setInternalRoleType(String internalRoleType) {
		this.internalRoleType = internalRoleType;
		return this;
	}

	public String getInternalRoleTypeName() {
		return internalRoleTypeName;
	}

	public RoleVO setInternalRoleTypeName(String internalRoleTypeName) {
		this.internalRoleTypeName = internalRoleTypeName;
		return this;
	}

	public String getRoleUserMappingType() {
		return roleUserMappingType;
	}

	public RoleVO setRoleUserMappingType(String roleUserMappingType) {
		this.roleUserMappingType = roleUserMappingType;
		return this;
	}

	public String getRoleUserMappingTypeName() {
		return roleUserMappingTypeName;
	}

	public RoleVO setRoleUserMappingTypeName(String roleUserMappingTypeName) {
		this.roleUserMappingTypeName = roleUserMappingTypeName;
		return this;
	}

	@Override
	public RoleVO set(RolePO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setName(entity.getName())
			.setInternalRoleType(entity.getInternalRoleType().toString())
			.setInternalRoleTypeName(entity.getInternalRoleType().getName())
			.setRoleUserMappingType(entity.getRoleUserMappingType().toString())
			.setRoleUserMappingTypeName(entity.getRoleUserMappingType().getName());
		return this;
	}

}
