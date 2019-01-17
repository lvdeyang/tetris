package com.sumavision.tetris.system.role;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 用户系统角色映射<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年1月15日 下午4:58:06
 */
@Entity
@Table(name = "TETRIS_USER_SYSTEM_ROLE_PERMISSION")
public class UserSystemRolePermissionPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;

	/** 用户id */
	private Long userId;
	
	/** 角色id */
	private Long roleId;

	@Column(name = "USER_ID")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "ROLE_ID")
	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	} 
	
}
