package com.sumavision.bvc.control.device.monitor.record;

import com.sumavision.bvc.device.monitor.record.MonitorRecordPO;
import com.sumavision.bvc.device.monitor.record.MonitorRecordType;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.commons.util.wrapper.StringBufferWrapper;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class MonitorRecordTaskVO extends AbstractBaseVO<MonitorRecordTaskVO, MonitorRecordPO>{

	/** 文件名称 */
	private String fileName;
	
	/** 视频源 */
	private String videoSource;
	
	/** 音频源 */
	private String audioSource;
	
	/** 任务开始时间 */
	private String startTime;
	
	/** 任务结束时间 */
	private String endTime;
	
	/** 任务模式 */
	private String mode;
	
	/** 录制模式，标识录制用户还是录制设备 */
	private String type;
	
	/** 录制用户id */
	private String recordUserId;

	/** 录制用户名 */
	private String recordUsername;
	
	/** 录制用户号码 */
	private String recordUserno;
	
	/** 录制状态*/
	private String status;
	
	public String getStatus() {
		return status;
	}

	public MonitorRecordTaskVO setStatus(String status) {
		this.status = status;
		return this;
	}

	public String getFileName() {
		return fileName;
	}

	public MonitorRecordTaskVO setFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

	public String getVideoSource() {
		return videoSource;
	}

	public MonitorRecordTaskVO setVideoSource(String videoSource) {
		this.videoSource = videoSource;
		return this;
	}

	public String getAudioSource() {
		return audioSource;
	}

	public MonitorRecordTaskVO setAudioSource(String audioSource) {
		this.audioSource = audioSource;
		return this;
	}

	public String getStartTime() {
		return startTime;
	}

	public MonitorRecordTaskVO setStartTime(String startTime) {
		this.startTime = startTime;
		return this;
	}

	public String getEndTime() {
		return endTime;
	}

	public MonitorRecordTaskVO setEndTime(String endTime) {
		this.endTime = endTime;
		return this;
	}

	public String getMode() {
		return mode;
	}

	public MonitorRecordTaskVO setMode(String mode) {
		this.mode = mode;
		return this;
	}
	
	public String getType() {
		return type;
	}

	public MonitorRecordTaskVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getRecordUserId() {
		return recordUserId;
	}

	public MonitorRecordTaskVO setRecordUserId(String recordUserId) {
		this.recordUserId = recordUserId;
		return this;
	}

	public String getRecordUsername() {
		return recordUsername;
	}

	public MonitorRecordTaskVO setRecordUsername(String recordUsername) {
		this.recordUsername = recordUsername;
		return this;
	}

	public String getRecordUserno() {
		return recordUserno;
	}

	public MonitorRecordTaskVO setRecordUserno(String recordUserno) {
		this.recordUserno = recordUserno;
		return this;
	}

	@Override
	public MonitorRecordTaskVO set(MonitorRecordPO entity) throws Exception {
		this.setId(entity.getId())
			.setStatus(entity.getStatus()==null?"":entity.getStatus().getName())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setFileName(entity.getFileName())
			.setVideoSource(new StringBufferWrapper().append(entity.getVideoBundleName()).append("-").append(entity.getVideoChannelId()).toString())
			.setAudioSource(new StringBufferWrapper().append(entity.getAudioBundleName()==null?"":entity.getAudioBundleName()).append("-").append(entity.getAudioChannelId()==null?"":entity.getAudioChannelId()).toString())
			.setStartTime(DateUtil.format(entity.getStartTime(), DateUtil.dateTimePattern))
			.setEndTime(entity.getEndTime()==null?"-":DateUtil.format(entity.getEndTime(), DateUtil.dateTimePattern))
			.setMode(entity.getMode().getName())
			.setType(entity.getType()==null?MonitorRecordType.LOCAL_DEVICE.getName():entity.getType().getName())
			.setRecordUserId(entity.getRecordUserId()==null?"-":entity.getRecordUserId().toString())
			.setRecordUsername(entity.getRecordUsername()==null?"-":entity.getRecordUsername())
			.setRecordUserno(entity.getRecordUserno()==null?"-":entity.getRecordUserno());
		return this;
	}

	
}
