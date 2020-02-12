package com.suma.venus.alarmoprlog.orm.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "oprlogpo")
public class OprlogPO extends CommonPO<OprlogPO> {

	private static final long serialVersionUID = -1574243620613297117L;

	@Column(name = "username", length = 30)
	private String userName;

	@Column(name = "oprname", length = 30)
	private String oprName;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "oprtime")
	private Date oprTime;

	@Column(name = "ip", length = 30)
	private String ip;

	@Column(name = "sourceservice", length = 30)
	private String sourceService;

	@Column(name = "oprdetail", length = 1024)
	private String oprDetail;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getOprName() {
		return oprName;
	}

	public void setOprName(String oprName) {
		this.oprName = oprName;
	}

	public String getOprDetail() {
		return oprDetail;
	}

	public void setOprDetail(String oprDetail) {
		this.oprDetail = oprDetail;
	}

	public void setSourceService(String sourceService) {
		this.sourceService = sourceService;
	}

	public Date getOprTime() {
		return oprTime;
	}

	public void setOprTime(Date oprTime) {
		this.oprTime = oprTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getSourceService() {
		return sourceService;
	}
}
