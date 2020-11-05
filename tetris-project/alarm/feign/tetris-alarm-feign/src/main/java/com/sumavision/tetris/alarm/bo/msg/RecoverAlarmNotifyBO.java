package com.sumavision.tetris.alarm.bo.msg;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.sumavision.tetris.alarm.bo.AlarmParamBOBase;

public class RecoverAlarmNotifyBO extends AlarmParamBOBase {

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date recoverTime;

	private String recoverStatus;

	private Long alarmId;

	public Date getRecoverTime() {
		return recoverTime;
	}

	public void setRecoverTime(Date recoverTime) {
		this.recoverTime = recoverTime;
	}

	public String getRecoverStatus() {
		return recoverStatus;
	}

	public void setRecoverStatus(String recoverStatus) {
		this.recoverStatus = recoverStatus;
	}

	public Long getAlarmId() {
		return alarmId;
	}

	public void setAlarmId(Long alarmId) {
		this.alarmId = alarmId;
	}

}
