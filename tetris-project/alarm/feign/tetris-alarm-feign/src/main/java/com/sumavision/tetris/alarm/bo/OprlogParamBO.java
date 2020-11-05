package com.sumavision.tetris.alarm.bo;

import java.util.Calendar;
import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

public class OprlogParamBO {

	private String sourceService;

	private String userName;

	private String oprName;

	private String sourceServiceIP;

	private EOprlogType oprlogType;

	public enum EOprlogType {

		/**
		 * 用户操作
		 */
		USER_OPR("用户操作"),

		/**
		 * 用户上线
		 */
		USER_ONLINE("用户上线"),

		/**
		 * 设备上线
		 */
		DEVICE_ONLINE("设备上线"),

		/**
		 * 设备下线
		 */
		DEVICE_OFFLINE("设备下线"),

		/**
		 * 外域连接成功
		 */
		EXTERNAL_CONNECT("外域连接成功"),

		/**
		 * 外域连接断开
		 */
		EXTERNAL_DISCONNECT("外域连接断开");

		private String name;

		private EOprlogType(String name) {
			this.name = name;
		}
		
		public String getEnumName() {
			return name;
		}
	}

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
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

	public String getSourceServiceIP() {
		return sourceServiceIP;
	}

	public void setSourceServiceIP(String sourceServiceIP) {
		this.sourceServiceIP = sourceServiceIP;
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

	public EOprlogType getOprlogType() {
		return oprlogType;
	}

	public void setOprlogType(EOprlogType oprlogType) {
		this.oprlogType = oprlogType;
	}

}
