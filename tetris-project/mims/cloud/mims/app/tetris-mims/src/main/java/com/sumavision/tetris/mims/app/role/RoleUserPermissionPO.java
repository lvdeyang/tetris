package com.sumavision.tetris.mims.app.role;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 用户角色<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2018年11月21日 下午12:02:04
 */
@Entity
@Table(name = "MIMS_ROLE_USER_PERMISSION")
public class RoleUserPermissionPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 关联用户id */
	private String userId;
	
	/** 关联角色id */
	private Long RoleId;

	@Column(name = "USER_ID")
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Column(name = "ROLE_ID")
	public Long getRoleId() {
		return RoleId;
	}

	public void setRoleId(Long roleId) {
		RoleId = roleId;
	}
	
}
