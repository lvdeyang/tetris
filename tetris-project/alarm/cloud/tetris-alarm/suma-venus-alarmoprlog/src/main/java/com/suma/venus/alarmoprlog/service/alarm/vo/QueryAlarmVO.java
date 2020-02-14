package com.suma.venus.alarmoprlog.service.alarm.vo;

/**
 * 
 * 告警查询条件 封装VO
 * 
 * 
 * @author chenmo
 *
 */
public class QueryAlarmVO {

	private Long id;

	private String sourceIP;

	private String sourceService;

	private String sourceObj;

	private String alarmLevel;

	private String lastCreateTime;

	private String firstCreateTime;

	private String recoverTime;

	private Integer alarmCount;

	private String alarmCode;

	private String alarmStatusArrString;

	private String alarmName;

	private String alarmParams;

	private String alarmBlockStatus;

	private Integer pageIndex;

	private Integer pageSize;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSourceIP() {
		return sourceIP;
	}

	public void setSourceIP(String sourceIP) {
		this.sourceIP = sourceIP;
	}

	public String getAlarmLevel() {
		return alarmLevel;
	}

	public void setAlarmLevel(String alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	public String getLastCreateTime() {
		return lastCreateTime;
	}

	public void setLastCreateTime(String lastCreateTime) {
		this.lastCreateTime = lastCreateTime;
	}

	public String getFirstCreateTime() {
		return firstCreateTime;
	}

	public void setFirstCreateTime(String firstCreateTime) {
		this.firstCreateTime = firstCreateTime;
	}

	public String getRecoverTime() {
		return recoverTime;
	}

	public void setRecoverTime(String recoverTime) {
		this.recoverTime = recoverTime;
	}

	public Integer getAlarmCount() {
		return alarmCount;
	}

	public void setAlarmCount(Integer alarmCount) {
		this.alarmCount = alarmCount;
	}

	public String getAlarmCode() {
		return alarmCode;
	}

	public void setAlarmCode(String alarmCode) {
		this.alarmCode = alarmCode;
	}

	public String getAlarmStatusArrString() {
		return alarmStatusArrString;
	}

	public void setAlarmStatusArrString(String alarmStatusArrString) {
		this.alarmStatusArrString = alarmStatusArrString;
	}

	public String getAlarmName() {
		return alarmName;
	}

	public void setAlarmName(String alarmName) {
		this.alarmName = alarmName;
	}

	public String getAlarmParams() {
		return alarmParams;
	}

	public void setAlarmParams(String alarmParams) {
		this.alarmParams = alarmParams;
	}

	public String getSourceService() {
		return sourceService;
	}

	public void setSourceService(String sourceService) {
		this.sourceService = sourceService;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public String getSourceObj() {
		return sourceObj;
	}

	public void setSourceObj(String sourceObj) {
		this.sourceObj = sourceObj;
	}

	public String getAlarmBlockStatus() {
		return alarmBlockStatus;
	}

	public void setAlarmBlockStatus(String alarmBlockStatus) {
		this.alarmBlockStatus = alarmBlockStatus;
	}

}
