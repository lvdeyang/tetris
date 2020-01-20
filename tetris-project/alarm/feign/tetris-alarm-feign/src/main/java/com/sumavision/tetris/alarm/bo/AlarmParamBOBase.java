package com.sumavision.tetris.alarm.bo;

import java.util.Map;

public class AlarmParamBOBase {

	private String alarmCode;

	private String sourceService;

	private String sourceServiceIP;

	private String alarmDevice;

	private String alarmObj;

	private Map<String, String> params;

	private String alarmStatus;

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

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public String getSourceServiceIP() {
		return sourceServiceIP;
	}

	public void setSourceServiceIP(String sourceServiceIP) {
		this.sourceServiceIP = sourceServiceIP;
	}

	public String getAlarmDevice() {
		return alarmDevice;
	}

	public void setAlarmDevice(String alarmDevice) {
		this.alarmDevice = alarmDevice;
	}

	public String getAlarmObj() {
		return alarmObj;
	}

	public void setAlarmObj(String alarmObj) {
		this.alarmObj = alarmObj;
	}

	public String getAlarmStatus() {
		return alarmStatus;
	}

	public void setAlarmStatus(String alarmStatus) {
		this.alarmStatus = alarmStatus;
	}

}
