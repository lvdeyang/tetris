package com.suma.venus.alarmoprlog.service.alarm.vo;

import com.suma.venus.alarmoprlog.orm.entity.AlarmPO;
import com.suma.venus.alarmoprlog.orm.entity.SubscribeAlarmPO;

public class AlarmRetryNotifyVO {

	private AlarmPO alarmPO;

	private SubscribeAlarmPO subscribeAlarmPO;

	private int retryNum = 0;

	public AlarmRetryNotifyVO(AlarmPO alarmPO, SubscribeAlarmPO subscribeAlarmPO) {
		this.alarmPO = alarmPO;
		this.subscribeAlarmPO = subscribeAlarmPO;
	}

	public AlarmPO getAlarmPO() {
		return alarmPO;
	}

	public void setAlarmPO(AlarmPO alarmPO) {
		this.alarmPO = alarmPO;
	}

	public SubscribeAlarmPO getSubscribeAlarmPO() {
		return subscribeAlarmPO;
	}

	public void setSubscribeAlarmPO(SubscribeAlarmPO subscribeAlarmPO) {
		this.subscribeAlarmPO = subscribeAlarmPO;
	}

	public int getRetryNum() {
		return retryNum;
	}

	public void setRetryNum(int retryNum) {
		this.retryNum = retryNum;
	}
}
