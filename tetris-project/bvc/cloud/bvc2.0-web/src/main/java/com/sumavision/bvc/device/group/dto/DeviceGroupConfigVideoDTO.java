package com.sumavision.bvc.device.group.dto;

import java.util.Date;

import com.sumavision.bvc.device.group.enumeration.ScreenLayout;
import com.sumavision.bvc.device.group.enumeration.VideoOperationType;

public class DeviceGroupConfigVideoDTO {
	
	private Long id;
	
	private String uuid;
	
	private Date updateTime;

	private String name;
	
	private VideoOperationType videoOperation;
	
	private String websiteDraw;
	
	private ScreenLayout layout;
	
	private Boolean record;

	public DeviceGroupConfigVideoDTO(){}
	
	public DeviceGroupConfigVideoDTO(Long id, String uuid, Date updateTime, String name, VideoOperationType videoOperationType, String websiteDraw){
		this.id = id;
		this.uuid = uuid;
		this.updateTime = updateTime;
		this.name = name;
		this.videoOperation = videoOperationType;
		this.websiteDraw = websiteDraw;
	}
	
	public DeviceGroupConfigVideoDTO(Long id, String uuid, Date updateTime, String name, VideoOperationType videoOperationType, String websiteDraw, ScreenLayout layout, Boolean record){
		this.id = id;
		this.uuid = uuid;
		this.updateTime = updateTime;
		this.name = name;
		this.videoOperation = videoOperationType;
		this.websiteDraw = websiteDraw;
		this.layout = layout;
		this.record = record;
	}
	
	public Long getId() {
		return id;
	}

	public DeviceGroupConfigVideoDTO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getUuid() {
		return uuid;
	}

	public DeviceGroupConfigVideoDTO setUuid(String uuid) {
		this.uuid = uuid;
		return this;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public DeviceGroupConfigVideoDTO setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
		return this;
	}

	public String getName() {
		return name;
	}

	public DeviceGroupConfigVideoDTO setName(String name) {
		this.name = name;
		return this;
	}

	public VideoOperationType getVideoOperation() {
		return videoOperation;
	}

	public DeviceGroupConfigVideoDTO setVideoOperation(VideoOperationType videoOperation) {
		this.videoOperation = videoOperation;
		return this;
	}

	public String getWebsiteDraw() {
		return websiteDraw;
	}

	public DeviceGroupConfigVideoDTO setWebsiteDraw(String websiteDraw) {
		this.websiteDraw = websiteDraw;
		return this;
	}

	public ScreenLayout getLayout() {
		return layout;
	}

	public DeviceGroupConfigVideoDTO setLayout(ScreenLayout layout) {
		this.layout = layout;
		return this;
	}

	public Boolean getRecord() {
		return record;
	}

	public DeviceGroupConfigVideoDTO setRecord(Boolean record) {
		this.record = record;
		return this;
	}
	
}
