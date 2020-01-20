package com.sumavision.tetris.alarm.bo.http;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.sumavision.tetris.alarm.bo.AlarmParamBOBase;

public class AlarmNotifyBO extends AlarmParamBOBase {

	private Long alarmId;


	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date alarmTime;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date recoverTime;

	public Long getAlarmId() {
		return alarmId;
	}

	public void setAlarmId(Long alarmId) {
		this.alarmId = alarmId;
	}

	public Date getAlarmTime() {
		return alarmTime;
	}

	public void setAlarmTime(Date alarmTime) {
		this.alarmTime = alarmTime;
	}

	public Date getRecoverTime() {
		return recoverTime;
	}

	public void setRecoverTime(Date recoverTime) {
		this.recoverTime = recoverTime;
	}

}
