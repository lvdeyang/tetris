package com.sumavision.tetris.alarm.bo.msg;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.sumavision.tetris.alarm.bo.AlarmParamBOBase;

public class TriggerAlarmNotifyParamBO extends AlarmParamBOBase {

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	private Long alarmId;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getAlarmId() {
		return alarmId;
	}

	public void setAlarmId(Long alarmId) {
		this.alarmId = alarmId;
	}

}
