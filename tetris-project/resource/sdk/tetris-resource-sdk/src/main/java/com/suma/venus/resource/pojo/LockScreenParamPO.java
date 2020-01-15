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
public class LockScreenParamPO extends CommonPO<LockScreenParamPO>{

	private String bundleId;
	
	private String screenId;
	
	/*记录屏当前被锁定的参数*/
	private String screenParam;

	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	public String getScreenId() {
		return screenId;
	}

	public void setScreenId(String screenId) {
		this.screenId = screenId;
	}

	@Type(type="text")
	@Column(name="screen_param",length=20000)
	public String getScreenParam() {
		return screenParam;
	}

	public void setScreenParam(String screenParam) {
		this.screenParam = screenParam;
	}
	
}
