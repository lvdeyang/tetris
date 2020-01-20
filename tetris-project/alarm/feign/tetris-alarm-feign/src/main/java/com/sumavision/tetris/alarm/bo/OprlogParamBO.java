package com.sumavision.tetris.alarm.bo;

import java.util.Calendar;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

public class OprlogParamBO {

	private String sourceService;

	private String userName;

	private String oprName;

	private String ip;
	
	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date oprTime = Calendar.getInstance().getTime();

	private String oprDetail;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getSourceService() {
		return sourceService;
	}

	public void setSourceService(String sourceService) {
		this.sourceService = sourceService;
	}

	public String getOprName() {
		return oprName;
	}

	public void setOprName(String oprName) {
		this.oprName = oprName;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Date getOprTime() {
		return oprTime;
	}

	public void setOprTime(Date oprTime) {
		this.oprTime = oprTime;
	}

	public String getOprDetail() {
		return oprDetail;
	}

	public void setOprDetail(String oprDetail) {
		this.oprDetail = oprDetail;
	}

}
