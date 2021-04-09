package com.sumavision.tetris.bvc.model.role;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 角色通道<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月4日 下午3:23:16
 */
@Entity
@Table(name = "TETRIS_BVC_MODEL_ROLE_CHANNEL")
public class RoleChannelPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 通道名称 */
	private String name;
	
	/** 通道类型 */
	private RoleChannelType type;
	
	/** 隶属角色id */
	private Long roleId;

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "TYPE")
	@Enumerated(value = EnumType.STRING)
	public RoleChannelType getType() {
		return type;
	}

	public void setType(RoleChannelType type) {
		this.type = type;
	}

	@Column(name = "ROLE_ID")
	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	
}
