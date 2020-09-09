package com.sumavision.tetris.bvc.business.terminal.hall;

import javax.jdo.annotations.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 会场授权<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年8月5日 下午1:28:25
 */
@Entity
@Table(name = "TETRIS_BVC_BUSINESS_CONFERENCE_HALL_ROLE_PERMISSION")
public class ConferenceHallRolePermissionPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 业务角色id */
	private Long roleId;
	
	/** 会场id */
	private Long conferenceHallId;
	
	/** 权限类型 */
	private PrivilegeType privilegeType;

	@Column(name = "ROLE_ID")
	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	
	@Column(name = "CONFERENCE_HALL_ID")
	public Long getConferenceHallId() {
		return conferenceHallId;
	}

	public void setConferenceHallId(Long conferenceHallId) {
		this.conferenceHallId = conferenceHallId;
	}

	@Enumerated(value = EnumType.STRING)
	@Column(name = "PRIVILEGE_TYPE")
	public PrivilegeType getPrivilegeType() {
		return privilegeType;
	}

	public void setPrivilegeType(PrivilegeType privilegeType) {
		this.privilegeType = privilegeType;
	}
	
}
