package com.suma.venus.resource.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.annotations.Type;

/**
 * 屏锁定参数信息
 * @author lxw
 *
 */
@Entity
public class LockBundleParamPO extends CommonPO<LockBundleParamPO>{

	private String bundleId;

	private Long userId;
	
	/**记录bundle当前被业务使用的pass_by_str参数*/
	private String passByStr;

	@Column(unique=true)
	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Type(type="text")
	@Column(name="pass_by_str",length=20000)
	public String getPassByStr() {
		return passByStr;
	}

	public void setPassByStr(String passByStr) {
		this.passByStr = passByStr;
	}
	
}
