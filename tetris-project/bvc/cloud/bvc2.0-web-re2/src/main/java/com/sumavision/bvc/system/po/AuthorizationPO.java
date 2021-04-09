package com.sumavision.bvc.system.po;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.sumavision.bvc.device.group.po.DeviceGroupAuthorizationMemberPO;
import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 看会权限模板<br/>
 * @author wjw
 * @date 2019年1月8日 下午2:44:20 
 */
@Entity
@Table(name = "BVC_SYSTEM_AUTHORIZATION")
public class AuthorizationPO extends AbstractBasePO{

	private static final long serialVersionUID = 1L;
	
	/** 权限名称 */
	private String name;
	
	/** 备注 */
	private String remark;
	
	/** 授权成员 */
	private Set<AuthorizationMemberPO> authorizationMembers = new HashSet<AuthorizationMemberPO>();

	@Column(name = "NAME")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "REMARK")
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@OneToMany(mappedBy = "authorization", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	public Set<AuthorizationMemberPO> getAuthorizationMembers() {
		return authorizationMembers;
	}

	public void setAuthorizationMembers(Set<AuthorizationMemberPO> authorizationMembers) {
		this.authorizationMembers = authorizationMembers;
	}
}
