package com.sumavision.bvc.device.monitor.record;

import java.util.Date;

import com.sumavision.tetris.commons.util.date.DateUtil;

public class MonitorRecordManyTimesVO {
	
	/** MonitorRecordManyTimesPO的主键*/
	private Long id;
	
	/** 文件名*/
	private String fileName;
	
	/** 开始时间*/
	private String startTime;
	
	/** 结束时间*/
	private String endTime;
	
	/** 文件下载地址*/
	private String downLoadPath;
	
	/** 文件预览地址*/
	private String previewPath;
	
	/** 录制状态*/
	private MonitorRecordStatus status;

	public Long getId() {
		return id;
	}

	public MonitorRecordManyTimesVO setId(Long id) {
		this.id = id;
		return this;
	}

	public String getFileName() {
		return fileName;
	}

	public MonitorRecordManyTimesVO setFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

	public String getStartTime() {
		return startTime;
	}

	public MonitorRecordManyTimesVO setStartTime(String startTime) {
		this.startTime = startTime;
		return this;
	}

	public String getEndTime() {
		return endTime;
	}

	public MonitorRecordManyTimesVO setEndTime(String endTime) {
		this.endTime = endTime;
		return this;
	}

	public String getDownLoadPath() {
		return downLoadPath;
	}

	public MonitorRecordManyTimesVO setDownLoadPath(String downLoadPath) {
		this.downLoadPath = downLoadPath;
		return this;
	}

	public String getPreviewPath() {
		return previewPath;
	}

	public MonitorRecordManyTimesVO setPreviewPath(String previewPath) {
		this.previewPath = previewPath;
		return this;
	}

	public MonitorRecordStatus getStatus() {
		return status;
	}

	public MonitorRecordManyTimesVO setStatus(MonitorRecordStatus status) {
		this.status = status;
		return this;
	}
	
	public MonitorRecordManyTimesVO set(MonitorRecordManyTimesPO recordmanyTimes,String fileName,String previewPath,String downLoadPath){
		this.id=recordmanyTimes.getId();
		this.fileName=fileName;
		this.startTime=recordmanyTimes.getStartTime()==null?null:DateUtil.format(recordmanyTimes.getStartTime());
		this.endTime=recordmanyTimes.getEndTime()==null?null:DateUtil.format(recordmanyTimes.getEndTime());
		this.downLoadPath=downLoadPath;
		this.previewPath=previewPath;
		this.status=recordmanyTimes.getStatus();
		return this;
	}
}
