package com.sumavision.tetris.alarm.bo.http;

import java.util.Calendar;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 订阅告警的 BO
 * 
 * @author chenmo
 *
 */
public class SubscribeParamBO {

	private String sourceService;

	private String alarmCode;

	private String subscribeIP;

	private String callbackUrl;

	private String alarmNotifyPattern;

	private String alarmNotifyMethod;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
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

	public String getSubscribeIP() {
		return subscribeIP;
	}

	public void setSubscribeIP(String subscribeIP) {
		this.subscribeIP = subscribeIP;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public String getAlarmNotifyMethod() {
		return alarmNotifyMethod;
	}

	public void setAlarmNotifyMethod(String alarmNotifyMethod) {
		this.alarmNotifyMethod = alarmNotifyMethod;
	}

	public String getAlarmNotifyPattern() {
		return alarmNotifyPattern;
	}

	public void setAlarmNotifyPattern(String alarmNotifyPattern) {
		this.alarmNotifyPattern = alarmNotifyPattern;
	}

}
