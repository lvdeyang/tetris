package com.sumavision.bvc.system.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * @ClassName: 看会权限成员
 * @author wjw
 * @date 2019年1月8日 下午2:00:57
 */
@Entity
@Table(name = "BVC_SYSTEM_AUTHORIZATION_MEMBER")
public class AuthorizationMemberPO extends AbstractBasePO{
	
	private static final long serialVersionUID = 1L;
	
	/** 设备id */
	private String bundleId;
	
	/** 关联设备组 */
	private AuthorizationPO authorization;

	@Column(name = "BUNDLE_ID")
	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	@ManyToOne
	@JoinColumn(name = "AUTHORIZATION_ID")
	public AuthorizationPO getAuthorization() {
		return authorization;
	}

	public void setAuthorization(AuthorizationPO authorization) {
		this.authorization = authorization;
	}
}
