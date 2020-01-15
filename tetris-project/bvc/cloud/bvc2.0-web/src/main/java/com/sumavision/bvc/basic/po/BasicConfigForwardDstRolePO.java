package com.sumavision.bvc.basic.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 通用议程转发目的<br/>
 * <b>作者:</b>wjw<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月12日 下午5:20:24
 */
@Entity
@Table(name = "BVC_BASIC_CONFIG_FORWARD_DST_ROLE")
public class BasicConfigForwardDstRolePO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 系统角色id */
	private Long roleId;
	
	/** 系统角色名 */
	private String roleName;
	
	/** 关联转发 */
	private BasicConfigForwardPO forward;

	@Column(name = "ROLE_ID")
	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	@Column(name = "ROLE_NAME")
	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@ManyToOne
	@JoinColumn(name = "FORWARD_ID")
	public BasicConfigForwardPO getForward() {
		return forward;
	}

	public void setForward(BasicConfigForwardPO forward) {
		this.forward = forward;
	}
	
}
