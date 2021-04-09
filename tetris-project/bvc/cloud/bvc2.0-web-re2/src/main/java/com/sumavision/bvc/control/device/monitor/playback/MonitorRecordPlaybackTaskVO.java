package com.sumavision.bvc.control.device.monitor.playback;

import com.sumavision.bvc.device.monitor.playback.MonitorRecordPlaybackTaskPO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class MonitorRecordPlaybackTaskVO extends AbstractBaseVO<MonitorRecordPlaybackTaskVO, MonitorRecordPlaybackTaskPO>{

	/** 文件名称 */
	private String fileName;
	
	/** 调阅任务类型 */
	private String type;
	
	/** 目标视频设备名称 */
	private String dstVideoBundleName;
	
	/** 目标视频通道名称 */
	private String dstVideoChannelName;
	
	/** 目标音频设备名称 */
	private String dstAudioBundleName;
	
	/** 目标音频通道名称 */
	private String dstAudioChannelName;
	
	/** osd id */
	private Long osdId;
	
	/** osd名称 */
	private String osdName;
	
	/** osd创建用户 */
	private String osdUsername;
	
	public String getFileName() {
		return fileName;
	}

	public MonitorRecordPlaybackTaskVO setFileName(String fileName) {
		this.fileName = fileName;
		return this;
	}

	public String getType() {
		return type;
	}

	public MonitorRecordPlaybackTaskVO setType(String type) {
		this.type = type;
		return this;
	}

	public String getDstVideoBundleName() {
		return dstVideoBundleName;
	}

	public MonitorRecordPlaybackTaskVO setDstVideoBundleName(String dstVideoBundleName) {
		this.dstVideoBundleName = dstVideoBundleName;
		return this;
	}

	public String getDstVideoChannelName() {
		return dstVideoChannelName;
	}

	public MonitorRecordPlaybackTaskVO setDstVideoChannelName(String dstVideoChannelName) {
		this.dstVideoChannelName = dstVideoChannelName;
		return this;
	}

	public String getDstAudioBundleName() {
		return dstAudioBundleName;
	}

	public MonitorRecordPlaybackTaskVO setDstAudioBundleName(String dstAudioBundleName) {
		this.dstAudioBundleName = dstAudioBundleName;
		return this;
	}

	public String getDstAudioChannelName() {
		return dstAudioChannelName;
	}

	public MonitorRecordPlaybackTaskVO setDstAudioChannelName(String dstAudioChannelName) {
		this.dstAudioChannelName = dstAudioChannelName;
		return this;
	}
	
	public Long getOsdId() {
		return osdId;
	}

	public MonitorRecordPlaybackTaskVO setOsdId(Long osdId) {
		this.osdId = osdId;
		return this;
	}

	public String getOsdName() {
		return osdName;
	}

	public MonitorRecordPlaybackTaskVO setOsdName(String osdName) {
		this.osdName = osdName;
		return this;
	}

	public String getOsdUsername() {
		return osdUsername;
	}

	public MonitorRecordPlaybackTaskVO setOsdUsername(String osdUsername) {
		this.osdUsername = osdUsername;
		return this;
	}

	@Override
	public MonitorRecordPlaybackTaskVO set(MonitorRecordPlaybackTaskPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setFileName(entity.getFileName())
			.setType(entity.getType().getName())
			.setDstVideoBundleName(entity.getDstVideoBundleName())
			.setDstVideoChannelName(entity.getDstVideoChannelName())
			.setDstAudioBundleName(entity.getDstAudioBundleName()==null?"-":entity.getDstAudioBundleName())
			.setDstAudioChannelName(entity.getDstAudioChannelName()==null?"-":entity.getDstAudioChannelName())
			.setOsdId(entity.getOsdId())
			.setOsdName("-")
			.setOsdUsername("-");
		return this;
	}
	
}
