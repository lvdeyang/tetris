package com.suma.venus.alarmoprlog.service.alarm.vo;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;

import com.suma.venus.alarmoprlog.orm.entity.SubscribeAlarmPO;
import com.suma.venus.alarmoprlog.orm.entity.AlarmInfoPO.EAlarmLevel;
import com.suma.venus.alarmoprlog.orm.entity.SubscribeAlarmPO.EAlarmNotifyPattern;

public class SubscribeAlarmVO {

	private Long id;

	private String alarmCode;

	private String alarmName;

	private EAlarmLevel alarmLevel;

	private String subServiceName;

	private String subsIP;

	private String subsObj;

	private String dstId;

	private Date subsTime;

	private EAlarmNotifyPattern alarmNotifyMode;

	public static SubscribeAlarmVO transFromPO(SubscribeAlarmPO po) {

		if (null == po) {
			return null;
		}

		SubscribeAlarmVO subscribeAlarmVO = new SubscribeAlarmVO();

		BeanUtils.copyProperties(po, subscribeAlarmVO);

		subscribeAlarmVO.setAlarmCode(po.getAlarmInfo().getAlarmCode());
		subscribeAlarmVO.setAlarmName(po.getAlarmInfo().getAlarmName());
		subscribeAlarmVO.setAlarmLevel(po.getAlarmInfo().getAlarmLevel());
		return subscribeAlarmVO;
	}

	public static List<SubscribeAlarmVO> transFromPOs(Collection<SubscribeAlarmPO> pos) {
		if (ObjectUtils.isEmpty(pos)) {
			return null;
		}

		List<SubscribeAlarmVO> vos = new LinkedList<>();
		for (SubscribeAlarmPO po : pos) {
			vos.add(transFromPO(po));
		}
		return vos;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAlarmCode() {
		return alarmCode;
	}

	public void setAlarmCode(String alarmCode) {
		this.alarmCode = alarmCode;
	}

	public String getAlarmName() {
		return alarmName;
	}

	public void setAlarmName(String alarmName) {
		this.alarmName = alarmName;
	}

	public EAlarmLevel getAlarmLevel() {
		return alarmLevel;
	}

	public void setAlarmLevel(EAlarmLevel alarmLevel) {
		this.alarmLevel = alarmLevel;
	}

	public String getSubServiceName() {
		return subServiceName;
	}

	public void setSubServiceName(String subServiceName) {
		this.subServiceName = subServiceName;
	}

	public String getSubsIP() {
		return subsIP;
	}

	public void setSubsIP(String subsIP) {
		this.subsIP = subsIP;
	}

	public String getSubsObj() {
		return subsObj;
	}

	public void setSubsObj(String subsObj) {
		this.subsObj = subsObj;
	}

	public String getDstId() {
		return dstId;
	}

	public void setDstId(String dstId) {
		this.dstId = dstId;
	}

	public Date getSubsTime() {
		return subsTime;
	}

	public void setSubsTime(Date subsTime) {
		this.subsTime = subsTime;
	}

	public void setAlarmNotifyMode(EAlarmNotifyPattern alarmNotifyMode) {
		this.alarmNotifyMode = alarmNotifyMode;
	}

	public EAlarmNotifyPattern getAlarmNotifyMode() {
		return alarmNotifyMode;
	}

}
