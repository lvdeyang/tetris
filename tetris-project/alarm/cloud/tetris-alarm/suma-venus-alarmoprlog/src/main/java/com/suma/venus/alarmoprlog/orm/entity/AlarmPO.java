/*
 *文件名：AlarmPO.java
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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

/**
 * 实时告警PO
 *
 * <p>
 * 实时告警PO，原始告警经过过滤，保存为实时告警信息。
 * 
 * @author chenmo
 * @see
 * @since
 */
@Entity
@Table(name = "alarmpo")
public class AlarmPO extends CommonPO<AlarmPO> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1684212325533629419L;

	/**
	 * 最新告警信息
	 */
	@OneToOne(fetch = FetchType.EAGER)
	@Fetch(value = FetchMode.JOIN)
	@JoinColumn(name = "lastalarm_id")
	private RawAlarmPO lastAlarm;

	/**
	 * 首次告警时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "firstcreatetime")
	private Date firstCreateTime;

	/**
	 * 告警次数
	 */
	@Column(name = "alarmcount")
	private Integer alarmCount;

	/**
	 * 告警状态
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "alarmstatus")
	private EAlarmStatus alarmStatus;

	/**
	 * 告警消除时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "recovertime")
	private Date recoverTime;

	/**
	 * 告警处理记录
	 */
	@Column(name = "recovermemo", length = 256)
	private String recoverMemo;

	/**
	 * 告警最后通知时间
	 */
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "lastnotifytime")
	private Date lastNotifyTime;

	public enum EAlarmStatus {

		/**
		 * 未处理
		 */
		UNTREATED,

		/**
		 * 已自动恢复
		 */
		AUTO_RECOVER,

		/**
		 * 已手动恢复
		 */
		MANUAL_RECOVER,

		/**
		 * 已忽略
		 */
		IGNORE,
		
		/**
		 * 单次告警，无需恢复 
		 */
		ONCE;
	}

	public RawAlarmPO getLastAlarm() {
		return lastAlarm;
	}

	public void setLastAlarm(RawAlarmPO lastAlarm) {
		this.lastAlarm = lastAlarm;
	}

	public Date getFirstCreateTime() {
		return firstCreateTime;
	}

	public void setFirstCreateTime(Date firstCreateTime) {
		this.firstCreateTime = firstCreateTime;
	}

	public Integer getAlarmCount() {
		return alarmCount;
	}

	public void setAlarmCount(Integer alarmCount) {
		this.alarmCount = alarmCount;
	}

	public EAlarmStatus getAlarmStatus() {
		return alarmStatus;
	}

	public void setAlarmStatus(EAlarmStatus alarmStatus) {
		this.alarmStatus = alarmStatus;
	}

	public Date getRecoverTime() {
		return recoverTime;
	}

	public void setRecoverTime(Date recoverTime) {
		this.recoverTime = recoverTime;
	}

	public String getRecoverMemo() {
		return recoverMemo;
	}

	public void setRecoverMemo(String recoverMemo) {
		this.recoverMemo = recoverMemo;
	}

	public Date getLastNotifyTime() {
		return lastNotifyTime;
	}

	public void setLastNotifyTime(Date lastNotifyTime) {
		this.lastNotifyTime = lastNotifyTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((alarmCount == null) ? 0 : alarmCount.hashCode());
		result = prime * result + ((alarmStatus == null) ? 0 : alarmStatus.hashCode());
		result = prime * result + ((firstCreateTime == null) ? 0 : firstCreateTime.hashCode());
		result = prime * result + ((lastAlarm == null) ? 0 : lastAlarm.hashCode());
		result = prime * result + ((lastNotifyTime == null) ? 0 : lastNotifyTime.hashCode());
		result = prime * result + ((recoverMemo == null) ? 0 : recoverMemo.hashCode());
		result = prime * result + ((recoverTime == null) ? 0 : recoverTime.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		AlarmPO other = (AlarmPO) obj;
		if (alarmCount == null) {
			if (other.alarmCount != null)
				return false;
		} else if (!alarmCount.equals(other.alarmCount))
			return false;
		if (alarmStatus != other.alarmStatus)
			return false;
		if (firstCreateTime == null) {
			if (other.firstCreateTime != null)
				return false;
		} else if (!firstCreateTime.equals(other.firstCreateTime))
			return false;
		if (lastAlarm == null) {
			if (other.lastAlarm != null)
				return false;
		} else if (!lastAlarm.equals(other.lastAlarm))
			return false;
		if (lastNotifyTime == null) {
			if (other.lastNotifyTime != null)
				return false;
		} else if (!lastNotifyTime.equals(other.lastNotifyTime))
			return false;
		if (recoverMemo == null) {
			if (other.recoverMemo != null)
				return false;
		} else if (!recoverMemo.equals(other.recoverMemo))
			return false;
		if (recoverTime == null) {
			if (other.recoverTime != null)
				return false;
		} else if (!recoverTime.equals(other.recoverTime))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AlarmPO [lastAlarm=" + lastAlarm + ", firstCreateTime=" + firstCreateTime + ", alarmCount=" + alarmCount
				+ ", alarmStatus=" + alarmStatus + ", recoverTime=" + recoverTime + ", recoverMemo=" + recoverMemo
				+ ", lastNotifyTime=" + lastNotifyTime + "]";
	}

}
