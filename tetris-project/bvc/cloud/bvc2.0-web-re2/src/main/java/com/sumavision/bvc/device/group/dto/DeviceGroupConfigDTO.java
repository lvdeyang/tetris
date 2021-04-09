package com.sumavision.bvc.device.group.dto;

import java.util.Date;

import com.sumavision.bvc.device.group.enumeration.AudioOperationType;
import com.sumavision.bvc.device.group.enumeration.ConfigType;

public class DeviceGroupConfigDTO {

	private Long id;
	
	private String uuid;
	
	private Date updateTime;
	
	private String name;
	
	private String remark;
	
	private ConfigType type;
	
	private Boolean run;
	
	private AudioOperationType audioOperation;
	
	public DeviceGroupConfigDTO(
			Long id, 
			String uuid, 
			Date updateTime, 
			String name, 
			String remark, 
			ConfigType type){
		this.id = id;
		this.uuid = uuid;
		this.updateTime = updateTime;
		this.name = name;
		this.remark = remark;
		this.type = type;
	}
	
	public DeviceGroupConfigDTO(
			Long id, 
			String uuid, 
			Date updateTime, 
			String name, 
			String remark, 
			ConfigType type,
			Boolean run,
			AudioOperationType audioOperation){
		this.id = id;
		this.uuid = uuid;
		this.updateTime = updateTime;
		this.name = name;
		this.remark = remark;
		this.type = type;
		this.run = run;
		this.audioOperation = audioOperation;
	}
	
	public Long getId() {
		return id;
	}

	public DeviceGroupConfigDTO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public DeviceGroupConfigDTO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public DeviceGroupConfigDTO setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public String getName() {
		return name;
	}

	public DeviceGroupConfigDTO setName(String name) {
		this.name = name;
		return this;
	}

	public String getRemark() {
		return remark;
	}

	public DeviceGroupConfigDTO setRemark(String remark) {
		this.remark = remark;
		return this;
	}

	public ConfigType getType() {
		return type;
	}

	public DeviceGroupConfigDTO setType(ConfigType type) {
		this.type = type;
		return this;
	}

	public Boolean getRun() {
		return run;
	}

	public DeviceGroupConfigDTO setRun(Boolean run) {
		this.run = run;
		return this;
	}

	public AudioOperationType getAudioOperation() {
		return audioOperation;
	}

	public DeviceGroupConfigDTO setAudioOperation(AudioOperationType audioOperation) {
		this.audioOperation = audioOperation;
		return this;
	}
	
}
