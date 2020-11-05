/*
 *文件名：RawAlarmPO.java
 *版权:
 *描述:
 *修改人:
 *修改时间：
 *修改内容：
 *
 */
package com.suma.venus.alarmoprlog.orm.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * 原始告警PO
 *
 * <p>
 * 原始告警PO
 * 
 * @author 陈默
 * @see
 * @since 1.0
 */
@Entity
@Table(name = "rawalarmpo")
public class RawAlarmPO extends CommonPO<RawAlarmPO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2199190613767371936L;

	/**
	 * 告警基本信息
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.JOIN)
	@JoinColumn(name = "alarminfo_id")
	private AlarmInfoPO alarmInfo;

	/**
	 * 告警产生时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createtime")
	private Date createTime;

	/**
	 * 来源微服务的IP
	 */
	@Column(name = "sourceserviceip", length = 30)
	private String sourceServiceIP;

	/**
	 * 来源微服务名称
	 */
	@Column(name = "sourceservice", length = 30)
	private String sourceService;

	/**
	 * 来源对象
	 */
	@Column(name = "alarmdevice", length = 1024)
	private String alarmDevice;

	/**
	 * 告警对象
	 */
	@Column(name = "alarmobj", length = 1024)
	private String alarmObj;

	/**
	 * 告警参数
	 */
	@Column(name = "alarmparams", length = 1024)
	private String alarmParams;

	/**
	 * 关联AlarmPO的id 方便能使用
	 * 
	 */
	@Column(name = "alarmpoid")
	private Long alarmPOId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AlarmInfoPO getAlarmInfo() {
		return alarmInfo;
	}

	public void setAlarmInfo(AlarmInfoPO alarmInfo) {
		this.alarmInfo = alarmInfo;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getSourceService() {
		return sourceService;
	}

	public void setSourceService(String sourceService) {
		this.sourceService = sourceService;
	}

	public String getAlarmParams() {
		return alarmParams;
	}

	public void setAlarmParams(String alarmParams) {
		this.alarmParams = alarmParams;
	}

	public Long getAlarmPOId() {
		return alarmPOId;
	}

	public void setAlarmPOId(Long alarmPOId) {
		this.alarmPOId = alarmPOId;
	}

	public String getSourceServiceIP() {
		return sourceServiceIP;
	}

	public void setSourceServiceIP(String sourceServiceIP) {
		this.sourceServiceIP = sourceServiceIP;
	}

	public String getAlarmDevice() {
		return alarmDevice;
	}

	public void setAlarmDevice(String alarmDevice) {
		this.alarmDevice = alarmDevice;
	}

	public String getAlarmObj() {
		return alarmObj;
	}

	public void setAlarmObj(String alarmObj) {
		this.alarmObj = alarmObj;
	}

}
