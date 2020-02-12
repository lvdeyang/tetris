<<<<<<< HEAD
/*
 *文件名：AlarmInfoPO.java
 *版权:
 *描述:
 *修改人:
 *修改时间：
 *修改内容：
 *
 */
package com.suma.venus.alarmoprlog.orm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * 告警基本信息PO
 *
 * <p>
 * 告警基本信息PO
 * 
 * @author chenmo
 * @see
 * @since
 */
@Entity
@Table(name = "alarminfopo")
public class AlarmInfoPO extends CommonPO<AlarmInfoPO> {

	private static final long serialVersionUID = -6623991486186362273L;

	/**
	 * 告警编码，由8位数字+字母组成
	 */
	@Column(name = "alarmcode", nullable = false, unique = true)
	private String alarmCode;

	/**
	 * 告警名称
	 */
	@Column(name = "alarmname", nullable = false)
	private String alarmName;

	/**
	 * 告警类型
	 */
	/*
	 * @Enumerated(EnumType.ORDINAL)
	 * 
	 * @Column(name = "alarmtype", nullable = false) private EAlarmType alarmType =
	 * EAlarmType.BUSINESS;
	 */

	/**
	 * 告警简介
	 */
	@Column(name = "alarmbrief", length = 1024)
	private String alarmBrief;

	/**
	 * 告警级别
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "alarmlevel", nullable = false)
	private EAlarmLevel alarmLevel = EAlarmLevel.MAJOR;

	/**
	 * 解决建议
	 */
	@Column(name = "alarmsolution", length = 1024)
	private String alarmSolution;

	/**
	 * 参数列表
	 */
	@Column(name = "alarmparamslist")
	private String alarmParamsList;

	/**
	 * 屏蔽状态
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "blockstatus", nullable = false)
	private EBlockStatus blockStatus = EBlockStatus.NORMAL;

	/**
	 * 收取邮件通知与否
	 */
	@Type(type = "yes_no")
	@Column(name = "emailnotify")
	private boolean emailNotify;

	/**
	 * 收取短信通知与否
	 */
	@Type(type = "yes_no")
	@Column(name = "smsnotify")
	private boolean SMSNotify;

	/**
	 * 是否可编辑
	 */
	@Type(type = "yes_no")
	@Column(name = "editable", nullable = false)
	private boolean editable = true;

	/**
	 * 告警分级
	 */
	public enum EAlarmLevel {

		/**
		 * 提示
		 */
		INFO,

		/**
		 * 一般
		 */
		MINOR,

		/**
		 * 重要
		 */
		MAJOR,

		/**
		 * 严重
		 */
		CRITICAL,
	}

	public enum EBlockStatus {

		/**
		 * 未屏蔽
		 */
		NORMAL,

		/**
		 * 已屏蔽
		 */
		BLOCKED,
	}

	public AlarmInfoPO() {

	}

	public AlarmInfoPO(String alarmCode, String alarmName, String alarmBrief, EAlarmLevel alarmLevel,
			String alarmSolution, String params) {
		this.alarmCode = alarmCode;
		this.alarmName = alarmName;
		this.alarmBrief = alarmBrief;
		this.alarmLevel = alarmLevel;
		this.alarmSolution = alarmSolution;
		this.alarmParamsList = params;
		this.blockStatus = EBlockStatus.NORMAL;
	}

	public String getAlarmCode() {
		return alarmCode;
	}

	public void setAlarmCode(String alarmCode) {
		this.alarmCode = alarmCode;
	}

	public String getAlarmName() {
		return alarmName;
	}

	public void setAlarmName(String alarmName) {
		this.alarmName = alarmName;
	}

	public String getAlarmBrief() {
		return alarmBrief;
	}

	public void setAlarmBrief(String alarmBrief) {
		this.alarmBrief = alarmBrief;
	}

	public EAlarmLevel getAlarmLevel() {
		return alarmLevel;
	}

	public void setAlarmLevel(EAlarmLevel alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	public String getAlarmSolution() {
		return alarmSolution;
	}

	public void setAlarmSolution(String alarmSolution) {
		this.alarmSolution = alarmSolution;
	}

	public String getAlarmParamsList() {
		return alarmParamsList;
	}

	public void setAlarmParamsList(String alarmParamsList) {
		this.alarmParamsList = alarmParamsList;
	}

	public EBlockStatus getBlockStatus() {
		return blockStatus;
	}

	public void setBlockStatus(EBlockStatus blockStatus) {
		this.blockStatus = blockStatus;
	}

	public Boolean getEmailNotify() {
		return emailNotify;
	}

	public void setEmailNotify(Boolean emailNotify) {
		this.emailNotify = emailNotify;
	}

	public Boolean getSMSNotify() {
		return SMSNotify;
	}

	public void setSMSNotify(Boolean sMSNotify) {
		SMSNotify = sMSNotify;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public void setEmailNotify(boolean emailNotify) {
		this.emailNotify = emailNotify;
	}

	public void setSMSNotify(boolean sMSNotify) {
		SMSNotify = sMSNotify;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (SMSNotify ? 1231 : 1237);
		result = prime * result + ((alarmBrief == null) ? 0 : alarmBrief.hashCode());
		result = prime * result + ((alarmCode == null) ? 0 : alarmCode.hashCode());
		result = prime * result + ((alarmLevel == null) ? 0 : alarmLevel.hashCode());
		result = prime * result + ((alarmName == null) ? 0 : alarmName.hashCode());
		result = prime * result + ((alarmParamsList == null) ? 0 : alarmParamsList.hashCode());
		result = prime * result + ((alarmSolution == null) ? 0 : alarmSolution.hashCode());
		result = prime * result + ((blockStatus == null) ? 0 : blockStatus.hashCode());
		result = prime * result + (editable ? 1231 : 1237);
		result = prime * result + (emailNotify ? 1231 : 1237);
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
		AlarmInfoPO other = (AlarmInfoPO) obj;
		if (SMSNotify != other.SMSNotify)
			return false;
		if (alarmBrief == null) {
			if (other.alarmBrief != null)
				return false;
		} else if (!alarmBrief.equals(other.alarmBrief))
			return false;
		if (alarmCode == null) {
			if (other.alarmCode != null)
				return false;
		} else if (!alarmCode.equals(other.alarmCode))
			return false;
		if (alarmLevel != other.alarmLevel)
			return false;
		if (alarmName == null) {
			if (other.alarmName != null)
				return false;
		} else if (!alarmName.equals(other.alarmName))
			return false;
		if (alarmParamsList == null) {
			if (other.alarmParamsList != null)
				return false;
		} else if (!alarmParamsList.equals(other.alarmParamsList))
			return false;
		if (alarmSolution == null) {
			if (other.alarmSolution != null)
				return false;
		} else if (!alarmSolution.equals(other.alarmSolution))
			return false;
		if (blockStatus != other.blockStatus)
			return false;
		if (editable != other.editable)
			return false;
		if (emailNotify != other.emailNotify)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AlarmInfoPO [alarmCode=" + alarmCode + ", alarmName=" + alarmName + ", alarmBrief=" + alarmBrief
				+ ", alarmLevel=" + alarmLevel + ", alarmSolution=" + alarmSolution + ", alarmParamsList="
				+ alarmParamsList + ", blockStatus=" + blockStatus + ", emailNotify=" + emailNotify + ", SMSNotify="
				+ SMSNotify + ", editable=" + editable + "]";
	}

}
=======
/*
 *文件名：AlarmInfoPO.java
 *版权:
 *描述:
 *修改人:
 *修改时间：
 *修改内容：
 *
 */
package com.suma.venus.alarmoprlog.orm.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

/**
 * 告警基本信息PO
 *
 * <p>
 * 告警基本信息PO
 * 
 * @author chenmo
 * @see
 * @since
 */
@Entity
@Table(name = "alarminfopo")
public class AlarmInfoPO extends CommonPO<AlarmInfoPO> {

	private static final long serialVersionUID = -6623991486186362273L;

	/**
	 * 告警编码，由8位数字组成
	 */
	@Column(name = "alarmcode", nullable = false, unique = true)
	private String alarmCode;

	/**
	 * 告警名称
	 */
	@Column(name = "alarmname", nullable = false)
	private String alarmName;

	/**
	 * 告警类型
	 */
	/*
	 * @Enumerated(EnumType.ORDINAL)
	 * 
	 * @Column(name = "alarmtype", nullable = false) private EAlarmType alarmType =
	 * EAlarmType.BUSINESS;
	 */

	/**
	 * 告警简介
	 */
	@Column(name = "alarmbrief", length = 1024)
	private String alarmBrief;

	/**
	 * 告警级别
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "alarmlevel", nullable = false)
	private EAlarmLevel alarmLevel = EAlarmLevel.MAJOR;

	/**
	 * 解决建议
	 */
	@Column(name = "alarmsolution", length = 1024)
	private String alarmSolution;

	/**
	 * 参数列表
	 */
	@Column(name = "alarmparamslist")
	private String alarmParamsList;

	/**
	 * 屏蔽状态
	 */
	@Enumerated(EnumType.STRING)
	@Column(name = "blockstatus", nullable = false)
	private EBlockStatus blockStatus = EBlockStatus.NORMAL;

	/**
	 * 收取邮件通知与否
	 */
	@Type(type = "yes_no")
	@Column(name = "emailnotify")
	private boolean emailNotify;

	/**
	 * 收取短信通知与否
	 */
	@Type(type = "yes_no")
	@Column(name = "smsnotify")
	private boolean SMSNotify;

	/**
	 * 是否可编辑
	 */
	@Type(type = "yes_no")
	@Column(name = "editable", nullable = false)
	private boolean editable = true;

	/**
	 * 告警分级
	 */
	public enum EAlarmLevel {

		/**
		 * 提示
		 */
		INFO,

		/**
		 * 一般
		 */
		MINOR,

		/**
		 * 重要
		 */
		MAJOR,

		/**
		 * 严重
		 */
		CRITICAL,
	}

	public enum EBlockStatus {

		/**
		 * 未屏蔽
		 */
		NORMAL,

		/**
		 * 已屏蔽
		 */
		BLOCKED,
	}

	public AlarmInfoPO() {

	}

	public AlarmInfoPO(String alarmCode, String alarmName, String alarmBrief, EAlarmLevel alarmLevel,
			String alarmSolution, String params) {
		this.alarmCode = alarmCode;
		this.alarmName = alarmName;
		this.alarmBrief = alarmBrief;
		this.alarmLevel = alarmLevel;
		this.alarmSolution = alarmSolution;
		this.alarmParamsList = params;
		this.blockStatus = EBlockStatus.NORMAL;
	}

	public String getAlarmCode() {
		return alarmCode;
	}

	public void setAlarmCode(String alarmCode) {
		this.alarmCode = alarmCode;
	}

	public String getAlarmName() {
		return alarmName;
	}

	public void setAlarmName(String alarmName) {
		this.alarmName = alarmName;
	}

	public String getAlarmBrief() {
		return alarmBrief;
	}

	public void setAlarmBrief(String alarmBrief) {
		this.alarmBrief = alarmBrief;
	}

	public EAlarmLevel getAlarmLevel() {
		return alarmLevel;
	}

	public void setAlarmLevel(EAlarmLevel alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	public String getAlarmSolution() {
		return alarmSolution;
	}

	public void setAlarmSolution(String alarmSolution) {
		this.alarmSolution = alarmSolution;
	}

	public String getAlarmParamsList() {
		return alarmParamsList;
	}

	public void setAlarmParamsList(String alarmParamsList) {
		this.alarmParamsList = alarmParamsList;
	}

	public EBlockStatus getBlockStatus() {
		return blockStatus;
	}

	public void setBlockStatus(EBlockStatus blockStatus) {
		this.blockStatus = blockStatus;
	}

	public Boolean getEmailNotify() {
		return emailNotify;
	}

	public void setEmailNotify(Boolean emailNotify) {
		this.emailNotify = emailNotify;
	}

	public Boolean getSMSNotify() {
		return SMSNotify;
	}

	public void setSMSNotify(Boolean sMSNotify) {
		SMSNotify = sMSNotify;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	public void setEmailNotify(boolean emailNotify) {
		this.emailNotify = emailNotify;
	}

	public void setSMSNotify(boolean sMSNotify) {
		SMSNotify = sMSNotify;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + (SMSNotify ? 1231 : 1237);
		result = prime * result + ((alarmBrief == null) ? 0 : alarmBrief.hashCode());
		result = prime * result + ((alarmCode == null) ? 0 : alarmCode.hashCode());
		result = prime * result + ((alarmLevel == null) ? 0 : alarmLevel.hashCode());
		result = prime * result + ((alarmName == null) ? 0 : alarmName.hashCode());
		result = prime * result + ((alarmParamsList == null) ? 0 : alarmParamsList.hashCode());
		result = prime * result + ((alarmSolution == null) ? 0 : alarmSolution.hashCode());
		result = prime * result + ((blockStatus == null) ? 0 : blockStatus.hashCode());
		result = prime * result + (editable ? 1231 : 1237);
		result = prime * result + (emailNotify ? 1231 : 1237);
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
		AlarmInfoPO other = (AlarmInfoPO) obj;
		if (SMSNotify != other.SMSNotify)
			return false;
		if (alarmBrief == null) {
			if (other.alarmBrief != null)
				return false;
		} else if (!alarmBrief.equals(other.alarmBrief))
			return false;
		if (alarmCode == null) {
			if (other.alarmCode != null)
				return false;
		} else if (!alarmCode.equals(other.alarmCode))
			return false;
		if (alarmLevel != other.alarmLevel)
			return false;
		if (alarmName == null) {
			if (other.alarmName != null)
				return false;
		} else if (!alarmName.equals(other.alarmName))
			return false;
		if (alarmParamsList == null) {
			if (other.alarmParamsList != null)
				return false;
		} else if (!alarmParamsList.equals(other.alarmParamsList))
			return false;
		if (alarmSolution == null) {
			if (other.alarmSolution != null)
				return false;
		} else if (!alarmSolution.equals(other.alarmSolution))
			return false;
		if (blockStatus != other.blockStatus)
			return false;
		if (editable != other.editable)
			return false;
		if (emailNotify != other.emailNotify)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AlarmInfoPO [alarmCode=" + alarmCode + ", alarmName=" + alarmName + ", alarmBrief=" + alarmBrief
				+ ", alarmLevel=" + alarmLevel + ", alarmSolution=" + alarmSolution + ", alarmParamsList="
				+ alarmParamsList + ", blockStatus=" + blockStatus + ", emailNotify=" + emailNotify + ", SMSNotify="
				+ SMSNotify + ", editable=" + editable + "]";
	}

}
>>>>>>> 63c7f8bd7a99af1df4351846716c8a97d533e4d1
