package com.suma.venus.alarmoprlog.service.alarm.vo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.suma.venus.alarmoprlog.orm.entity.AlarmInfoPO;
import com.suma.venus.alarmoprlog.orm.entity.AlarmPO;
import com.suma.venus.alarmoprlog.orm.entity.RawAlarmPO;
import com.suma.venus.alarmoprlog.orm.entity.AlarmInfoPO.EAlarmLevel;
import com.suma.venus.alarmoprlog.orm.entity.AlarmPO.EAlarmStatus;

/**
 * 实时告警VO 供页面实时告警列表显示
 *
 * <p>
 * 实时告警VO 共页面实时告警列表显示
 * 
 * @author 陈默 2014-3-17
 * @see
 * @since 1.0
 */
public class AlarmVO {

	private Long id;

	private String sourceServiceIP;

	private String sourceService;

	private String alarmLevel;

	private String alarmDevice;

	private String alarmObj;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date lastCreateTime;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date firstCreateTime;

	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	private Date recoverTime;

	private Integer alarmCount;

	private String alarmCode;

	private String alarmStatus;

	private String alarmName;

	private String alarmParams;

	private String alarmSolution;

	public static AlarmVO transFromPO(AlarmPO alarmPO, String locale) {
		if (alarmPO == null) {
			return null;
		}

		if (alarmPO.getLastAlarm() == null || alarmPO.getLastAlarm().getAlarmInfo() == null) {
			return null;
		}

		RawAlarmPO rawAlarmPO = alarmPO.getLastAlarm();
		AlarmInfoPO alarmInfoPO = alarmPO.getLastAlarm().getAlarmInfo();

		AlarmVO alarmVO = new AlarmVO();
		alarmVO.setId(alarmPO.getId());
		alarmVO.setAlarmCode(alarmInfoPO.getAlarmCode());
		alarmVO.setSourceServiceIP(rawAlarmPO.getSourceServiceIP());
		alarmVO.setSourceService(rawAlarmPO.getSourceService());
		alarmVO.setAlarmLevel(transAlarmLevel(alarmInfoPO.getAlarmLevel(), locale));
		alarmVO.setAlarmCount(alarmPO.getAlarmCount());
		alarmVO.setLastCreateTime(rawAlarmPO.getCreateTime());
		alarmVO.setFirstCreateTime(alarmPO.getFirstCreateTime());
		alarmVO.setAlarmStatus(transAlarmStatus(alarmPO.getAlarmStatus(), locale));
		alarmVO.setRecoverTime(alarmPO.getRecoverTime());
		alarmVO.setAlarmParams(rawAlarmPO.getAlarmParams());
		alarmVO.setAlarmName(alarmInfoPO.getAlarmName());
		alarmVO.setAlarmDevice(rawAlarmPO.getAlarmDevice());
		alarmVO.setAlarmObj(rawAlarmPO.getAlarmObj());
		alarmVO.setAlarmSolution(alarmInfoPO.getAlarmSolution());

		return alarmVO;
	}

	public static List<AlarmVO> transFromPOs(List<AlarmPO> alarmPOs, String locale) {
		if (alarmPOs == null || alarmPOs.isEmpty()) {
			return null;
		}

		List<AlarmVO> alarmVOs = new ArrayList<AlarmVO>();
		AlarmVO alarmVO = null;
		for (AlarmPO alarmPO : alarmPOs) {
			alarmVO = transFromPO(alarmPO, locale);
			if (alarmVO != null) {
				alarmVOs.add(alarmVO);
			}
		}

		if (alarmVOs.isEmpty()) {
			return null;
		}

		return alarmVOs;
	}

	private static String transAlarmStatus(EAlarmStatus status, String locale) {

		if (locale.equals("zh_CN")) {
			switch (status) {
			case UNTREATED:
				return "未处理";
			case AUTO_RECOVER:
				return "已自动恢复";
			case MANUAL_RECOVER:
				return "已手动恢复";
			case IGNORE:
				return "已忽略";
			case ONCE:
				return "不需处理";
			default:
				return null;
			}
		} else {
			switch (status) {
			case UNTREATED:
				return "UNTREATED";
			case AUTO_RECOVER:
				return "AUTO RECOVERED";
			case MANUAL_RECOVER:
				return "MANUAL RECOVERED";
			case IGNORE:
				return "IGNORED";
			case ONCE:
				return "ONCE";
			default:
				return null;
			}
		}
	}

	private static String transAlarmLevel(EAlarmLevel level, String locale) {
		if (locale.equals("zh_CN")) {
			switch (level) {
			case INFO:
				return "提示";
			case MINOR:
				return "一般";
			case MAJOR:
				return "紧急";
			case CRITICAL:
				return "严重";
			default:
				return null;
			}

		} else {
			return level.toString();
		}
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSourceService() {
		return sourceService;
	}

	public void setSourceService(String sourceService) {
		this.sourceService = sourceService;
	}

	public String getAlarmLevel() {
		return alarmLevel;
	}

	public void setAlarmLevel(String alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	public Date getLastCreateTime() {
		return lastCreateTime;
	}

	public void setLastCreateTime(Date lastCreateTime) {
		this.lastCreateTime = lastCreateTime;
	}

	public Date getFirstCreateTime() {
		return firstCreateTime;
	}

	public void setFirstCreateTime(Date firstCreateTime) {
		this.firstCreateTime = firstCreateTime;
	}

	public Date getRecoverTime() {
		return recoverTime;
	}

	public void setRecoverTime(Date recoverTime) {
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

	public String getAlarmStatus() {
		return alarmStatus;
	}

	public void setAlarmStatus(String alarmStatus) {
		this.alarmStatus = alarmStatus;
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

	public String getAlarmSolution() {
		return alarmSolution;
	}

	public void setAlarmSolution(String alarmSolution) {
		this.alarmSolution = alarmSolution;
	}

}
