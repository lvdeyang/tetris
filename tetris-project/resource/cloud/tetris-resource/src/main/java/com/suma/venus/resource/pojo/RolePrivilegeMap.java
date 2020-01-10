/*
 * 文件名：RolePrivilegeMap.java
 * 版权：Copyright 2013-2015 Sumavision. All Rights Reserved. 
 * 修改人: ManerFan
 * 修改时间：2015年12月18日
 *
 */
package com.suma.venus.resource.pojo;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 用户角色和权限映射<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年12月25日 下午4:17:21
 */
@Entity
@Table(name = "role_privilege_map")
public class RolePrivilegeMap extends CommonPO<RolePrivilegeMap>{

	private Long roleId;
	
	private Long privilegeId;

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getPrivilegeId() {
		return privilegeId;
	}

	public void setPrivilegeId(Long privilegeId) {
		this.privilegeId = privilegeId;
	}

}
