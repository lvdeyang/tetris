package com.sumavision.tetris.bvc.model.role;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

@Entity
@Table(name = "TETRIS_BVC_MODEL_ROLE")
public class RolePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 角色类型 */
	private String name;
	
	/** 内置角色id */
	private InternalRoleType internalRoleType;
	
	/** 隶属业务id */
	private Long businessId;
	
	/** 创建用户id */
	private String userId;
	
	/** 角色用户映射关系 */
	private RoleUserMappingType roleUserMappingType;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "INTERNAL_ROLE_TYPE")
	@Enumerated(value = EnumType.STRING)
	public InternalRoleType getInternalRoleType() {
		return internalRoleType;
	}

	public void setInternalRoleType(InternalRoleType internalRoleType) {
		this.internalRoleType = internalRoleType;
	}

	@Column(name = "BUSINESS_ID")
	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	@Column(name = "USER_ID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "ROLE_USER_MAPPING_TYPE")
	@Enumerated(value = EnumType.STRING)
	public RoleUserMappingType getRoleUserMappingType() {
		return roleUserMappingType;
	}

	public void setRoleUserMappingType(RoleUserMappingType roleUserMappingType) {
		this.roleUserMappingType = roleUserMappingType;
	}

}
