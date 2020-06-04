package com.sumavision.tetris.bvc.business.group;

import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 业务组成员与业务角色的映射<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2020年6月4日 下午3:38:45
 */
@Entity
@Table(name = "TETRIS_BVC_BUSINESS_GROUP_MEMBER_ROLE_PERMISSION")
public class GroupMemberRolePermissionPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 角色id */
	private Long roleId;
	
	/** 成员id */
	private Long groupMemberId;

	@Column(name = "ROLE_ID")
	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	@Column(name = "GROUP_MEMBER_ID")
	public Long getGroupMemberId() {
		return groupMemberId;
	}

	public void setGroupMemberId(Long groupMemberId) {
		this.groupMemberId = groupMemberId;
	}
	
}
