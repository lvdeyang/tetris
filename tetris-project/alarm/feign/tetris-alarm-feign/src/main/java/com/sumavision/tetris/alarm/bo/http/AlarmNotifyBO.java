package com.sumavision.tetris.alarm.bo.http;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.util.StringUtils;

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

	public static Map<String, String> formatParams(String params) {

		if (StringUtils.isEmpty(params)) {
			return null;
		}

		Map<String, String> paramsMap = new HashMap<>();

		String[] strArr = params.split(";");

		for (int i = 0; i < strArr.length; i++) {
			if (StringUtils.isEmpty(strArr[i])) {
				continue;
			}

			try {
				String[] kvArr = strArr[i].split("=");
				paramsMap.put(kvArr[0], kvArr[1]);
			} catch (Exception e) {
				// TODO: handle exception
			}

		}

		return paramsMap;

	}

}
