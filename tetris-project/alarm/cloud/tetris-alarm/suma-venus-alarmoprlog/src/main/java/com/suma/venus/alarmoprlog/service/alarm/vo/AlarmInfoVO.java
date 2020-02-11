package com.suma.venus.alarmoprlog.service.alarm.vo;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;

import com.suma.venus.alarmoprlog.orm.entity.AlarmInfoPO;
import com.suma.venus.alarmoprlog.orm.entity.AlarmInfoPO.EAlarmLevel;
import com.suma.venus.alarmoprlog.orm.entity.AlarmInfoPO.EBlockStatus;

public class AlarmInfoVO {

	private Long id;

	private String alarmCode;

	private String alarmName;

	private String alarmBrief;

	private EAlarmLevel alarmLevel;

	private String alarmSolution;

	private String alarmParamsList;

	private EBlockStatus blockStatus;

	private boolean emailNotify;

	private boolean SMSNotify;

	private boolean editable;

	public static AlarmInfoVO transFromPO(AlarmInfoPO po) {

		if (null == po) {
			return null;
		}

		AlarmInfoVO alarmInfoVO = new AlarmInfoVO();
		BeanUtils.copyProperties(po, alarmInfoVO);
		BeanUtils.copyProperties(po, alarmInfoVO, "");

		return alarmInfoVO;
	}

	public static List<AlarmInfoVO> transFromPOs(Collection<AlarmInfoPO> pos) {
		if (ObjectUtils.isEmpty(pos)) {
			return null;
		}

		List<AlarmInfoVO> vos = new LinkedList<>();
		for (AlarmInfoPO po : pos) {
			vos.add(transFromPO(po));
		}
		return vos;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public boolean isEmailNotify() {
		return emailNotify;
	}

	public void setEmailNotify(boolean emailNotify) {
		this.emailNotify = emailNotify;
	}

	public boolean isSMSNotify() {
		return SMSNotify;
	}

	public void setSMSNotify(boolean sMSNotify) {
		SMSNotify = sMSNotify;
	}

	public boolean isEditable() {
		return editable;
	}

	public void setEditable(boolean editable) {
		this.editable = editable;
	}

}
