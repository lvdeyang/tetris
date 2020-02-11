package com.sumavision.tetris.alarm.bo.msg;

import java.util.Calendar;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 订阅告警的 BO
 * 
 * @author chenmo
 *
 */
public class SubscribeRequestParamBO {

	private String sourceService;

	private String alarmCode;

	private String ip;
	
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date subscribeTime = Calendar.getInstance().getTime();

	public String getSourceService() {
		return sourceService;
	}

	public void setSourceService(String sourceService) {
		this.sourceService = sourceService;
	}

	public String getAlarmCode() {
		return alarmCode;
	}

	public void setAlarmCode(String alarmCode) {
		this.alarmCode = alarmCode;
	}

	public Date getSubscribeTime() {
		return subscribeTime;
	}

	public void setSubscribeTime(Date subscribeTime) {
		this.subscribeTime = subscribeTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
