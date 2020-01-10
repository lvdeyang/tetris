package com.suma.venus.resource.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


/**
 * 
 * @author bundle认证登陆黑名单
 *
 */
@Entity
public class BundleLoginBlackListPO extends CommonPO<BundleLoginBlackListPO>{

	private String loginId;
	
	/**加入黑名单的时间点*/
	private Date startTime;

	@Column(unique=true)
	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	@Temporal(value = TemporalType.TIMESTAMP)
	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	
}
