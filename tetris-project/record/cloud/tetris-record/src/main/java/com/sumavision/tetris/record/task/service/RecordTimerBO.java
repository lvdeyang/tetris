package com.sumavision.tetris.record.task.service;

import java.util.Date;

import com.sumavision.tetris.record.device.DeviceDAO;
import com.sumavision.tetris.record.device.DevicePO;
import com.sumavision.tetris.record.storage.StorageDAO;
import com.sumavision.tetris.record.storage.StoragePO;
import com.sumavision.tetris.record.strategy.RecordStrategyPO;

public class RecordTimerBO {

	private String sourceId;

	private String sourceUrl;

	private String sourceType;

	private Long deviceId;

	private String deviceIP;

	private Integer devicePort;

	private String recordStrategyName;

	private Long recordStrategyId;

	private String capacityTaskId;

	private String recordBasePath;

	private String filePath;

	private boolean isStart = true;

	private boolean isCut = false;

	private Date operateTime;

	public static RecordTimerBO transFromRecordStrategyPO(RecordStrategyPO recordStrategyPO, DeviceDAO deviceDAO,
			StorageDAO storageDAO) {

		DevicePO devicePO = deviceDAO.findOne(recordStrategyPO.getDeviceId());
		StoragePO storagePO = storageDAO.findOne(recordStrategyPO.getStorageId());

		RecordTimerBO recordTimerBO = new RecordTimerBO();
		recordTimerBO.setSourceId(recordStrategyPO.getSourceId());
		recordTimerBO.setSourceType(recordStrategyPO.getSourceType());
		recordTimerBO.setSourceUrl(recordStrategyPO.getSourceUrl());
		recordTimerBO.setDeviceId(recordStrategyPO.getDeviceId());
		recordTimerBO.setDeviceIP(devicePO.getDeviceIP());
		recordTimerBO.setDevicePort(devicePO.getDevicePort());
		recordTimerBO.setRecordBasePath(storagePO.getLocalRecordPath());
		recordTimerBO.setRecordStrategyId(recordStrategyPO.getId());
		recordTimerBO.setRecordStrategyName(recordStrategyPO.getName());
		return recordTimerBO;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	public Long getDeviceId() {
		return deviceId;
	}

	public String getDeviceIP() {
		return deviceIP;
	}

	public void setDeviceIP(String deviceIP) {
		this.deviceIP = deviceIP;
	}

	public Integer getDevicePort() {
		return devicePort;
	}

	public void setDevicePort(Integer devicePort) {
		this.devicePort = devicePort;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getSourceUrl() {
		return sourceUrl;
	}

	public void setSourceUrl(String sourceUrl) {
		this.sourceUrl = sourceUrl;
	}

	public String getSourceType() {
		return sourceType;
	}

	public void setSourceType(String sourceType) {
		this.sourceType = sourceType;
	}

	public String getRecordStrategyName() {
		return recordStrategyName;
	}

	public void setRecordStrategyName(String recordStrategyName) {
		this.recordStrategyName = recordStrategyName;
	}

	public boolean isStart() {
		return isStart;
	}

	public void setStart(boolean isStart) {
		this.isStart = isStart;
	}

	public Date getOperateTime() {
		return operateTime;
	}

	public void setOperateTime(Date operateTime) {
		this.operateTime = operateTime;
	}

	public boolean isCut() {
		return isCut;
	}

	public void setCut(boolean isCut) {
		this.isCut = isCut;
	}

	public Long getRecordStrategyId() {
		return recordStrategyId;
	}

	public void setRecordStrategyId(Long recordStrategyId) {
		this.recordStrategyId = recordStrategyId;
	}

	public String getCapacityTaskId() {
		return capacityTaskId;
	}

	public void setCapacityTaskId(String capacityTaskId) {
		this.capacityTaskId = capacityTaskId;
	}

	public String getRecordBasePath() {
		return recordBasePath;
	}

	public void setRecordBasePath(String recordBasePath) {
		this.recordBasePath = recordBasePath;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
