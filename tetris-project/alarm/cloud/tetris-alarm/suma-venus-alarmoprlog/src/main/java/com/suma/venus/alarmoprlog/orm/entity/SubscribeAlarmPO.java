/*
 *文件名：SubscribeAlarmPO.java
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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * 订阅告警PO
 *
 * <p>
 * 订阅告警PO
 * 
 * @author 陈默 2014-3-4
 * @see
 * @since 1.0
 */
@Entity
@Table(name = "subscribealarmpo")
public class SubscribeAlarmPO extends CommonPO<SubscribeAlarmPO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4014074990304326068L;

	/**
	 * 订阅通知地址
	 */
	@Column(name = "callbackUrl")
	private String callbackUrl;

	/**
	 * 订阅通知的消息服务ID
	 */
	@Column(name = "msgCallbackId")
	private String msgCallbackId;

	/**
	 * 订阅的告警信息
	 */
	@ManyToOne(targetEntity = AlarmInfoPO.class, fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.JOIN)
	@JoinColumn(name = "alarminfo")
	private AlarmInfoPO alarmInfo;

	/**
	 * 订阅IP
	 */
	@Column(name = "subsip", length = 30)
	private String subsIP;

	/**
	 * 订阅服务
	 */
	@Column(name = "subServiceName", length = 40)
	private String subServiceName;

	/**
	 * 订阅对象
	 */
	@Column(name = "subsobj")
	private String subsObj;

	/**
	 * 订阅时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "substime")
	private Date subsTime;

	/**
	 * 订阅有效期
	 */
	@Column(name = "subsvalidtime")
	private Integer subsValidTime;

	/**
	 * 告警通知模式
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "alarmnotifypattern")
	private EAlarmNotifyPattern alarmNotifyPattern;

	/**
	 * 告警通知方法(途径)
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "alarmnotifymethod")
	private EAlarmNotifyMethod alarmNotifyMethod;

	public enum EAlarmNotifyPattern {

		/**
		 * 正常通知
		 */
		NOTIFY_NORMAL,

		/**
		 * 每次都通知
		 */
		NOTIFY_EVERYTIME,
	}

	public enum EAlarmNotifyMethod {

		/**
		 * 普通HTTP通知
		 */
		HTTP,

		/**
		 * 通过spring cloud ribbon 负载均衡方式通知
		 */
		RIBBON_LOADBALANCED,

		/**
		 * 通过消息服务通知
		 */
		MESSAGE_SERVICE

	}

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

	public String getSubsIP() {
		return subsIP;
	}

	public void setSubsIP(String subsIP) {
		this.subsIP = subsIP;
	}

	public String getSubServiceName() {
		return subServiceName;
	}

	public void setSubServiceName(String subServiceName) {
		this.subServiceName = subServiceName;
	}

	public String getSubsObj() {
		return subsObj;
	}

	public void setSubsObj(String subsObj) {
		this.subsObj = subsObj;
	}

	public Date getSubsTime() {
		return subsTime;
	}

	public void setSubsTime(Date subsTime) {
		this.subsTime = subsTime;
	}

	public Integer getSubsValidTime() {
		return subsValidTime;
	}

	public void setSubsValidTime(Integer subsValidTime) {
		this.subsValidTime = subsValidTime;
	}

	public EAlarmNotifyPattern getAlarmNotifyPattern() {
		return alarmNotifyPattern;
	}

	public void setAlarmNotifyPattern(EAlarmNotifyPattern alarmNotifyPattern) {
		this.alarmNotifyPattern = alarmNotifyPattern;
	}

	public EAlarmNotifyMethod getAlarmNotifyMethod() {
		return alarmNotifyMethod;
	}

	public void setAlarmNotifyMethod(EAlarmNotifyMethod alarmNotifyMethod) {
		this.alarmNotifyMethod = alarmNotifyMethod;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public String getMsgCallbackId() {
		return msgCallbackId;
	}

	public void setMsgCallbackId(String msgCallbackId) {
		this.msgCallbackId = msgCallbackId;
	}

}
