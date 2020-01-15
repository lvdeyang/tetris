package com.sumavision.bvc.control.device.monitor.live;

import com.sumavision.bvc.device.monitor.live.device.MonitorLiveDevicePO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

/**
 * 类型概述<br/>
 * <p>详细描述</p>
 * <b>作者:</b>Administrator<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年4月26日 下午5:29:31
 */
public class MonitorLiveVO extends AbstractBaseVO<MonitorLiveVO, MonitorLiveDevicePO>{

	private String videoBundleName;
	
	private String videoChannelName;
	
	private String audioBundleName;
	
	private String audioChannelName;
	
	private String dstVideoBundleName;
	
	private String dstVideoChannelName;
	
	private String dstAudioBundleName;
	
	private String dstAudioChannelName;
	
	private Long osdId;
	
	private String osdName;
	
	private String osdUsername;
	
	private String type;
	
	public String getVideoBundleName() {
		return videoBundleName;
	}

	public MonitorLiveVO setVideoBundleName(String videoBundleName) {
		this.videoBundleName = videoBundleName;
		return this;
	}

	public String getVideoChannelName() {
		return videoChannelName;
	}

	public MonitorLiveVO setVideoChannelName(String videoChannelName) {
		this.videoChannelName = videoChannelName;
		return this;
	}

	public String getAudioBundleName() {
		return audioBundleName;
	}

	public MonitorLiveVO setAudioBundleName(String audioBundleName) {
		this.audioBundleName = audioBundleName;
		return this;
	}

	public String getAudioChannelName() {
		return audioChannelName;
	}

	public MonitorLiveVO setAudioChannelName(String audioChannelName) {
		this.audioChannelName = audioChannelName;
		return this;
	}

	public String getDstVideoBundleName() {
		return dstVideoBundleName;
	}

	public MonitorLiveVO setDstVideoBundleName(String dstVideoBundleName) {
		this.dstVideoBundleName = dstVideoBundleName;
		return this;
	}

	public String getDstVideoChannelName() {
		return dstVideoChannelName;
	}

	public MonitorLiveVO setDstVideoChannelName(String dstVideoChannelName) {
		this.dstVideoChannelName = dstVideoChannelName;
		return this;
	}

	public String getDstAudioBundleName() {
		return dstAudioBundleName;
	}

	public MonitorLiveVO setDstAudioBundleName(String dstAudioBundleName) {
		this.dstAudioBundleName = dstAudioBundleName;
		return this;
	}

	public String getDstAudioChannelName() {
		return dstAudioChannelName;
	}

	public MonitorLiveVO setDstAudioChannelName(String dstAudioChannelName) {
		this.dstAudioChannelName = dstAudioChannelName;
		return this;
	}
	
	public Long getOsdId() {
		return osdId;
	}

	public MonitorLiveVO setOsdId(Long osdId) {
		this.osdId = osdId;
		return this;
	}

	public String getOsdName() {
		return osdName;
	}

	public MonitorLiveVO setOsdName(String osdName) {
		this.osdName = osdName;
		return this;
	}

	public String getOsdUsername() {
		return osdUsername;
	}

	public MonitorLiveVO setOsdUsername(String osdUsername) {
		this.osdUsername = osdUsername;
		return this;
	}

	public String getType() {
		return type;
	}

	public MonitorLiveVO setType(String type) {
		this.type = type;
		return this;
	}

	@Override
	public MonitorLiveVO set(MonitorLiveDevicePO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setVideoBundleName(entity.getVideoBundleName())
			.setVideoChannelName(entity.getVideoChannelName())
			.setAudioBundleName(entity.getAudioBundleName()==null?"-":entity.getAudioBundleName())
			.setAudioChannelName(entity.getAudioChannelName()==null?"-":entity.getAudioChannelName())
			.setDstVideoBundleName(entity.getDstVideoBundleName())
			.setDstVideoChannelName(entity.getDstVideoChannelName())
			.setDstAudioBundleName(entity.getDstAudioBundleName()==null?"-":entity.getDstAudioBundleName())
			.setDstAudioChannelName(entity.getAudioChannelName()==null?"-":entity.getAudioChannelName())
			.setOsdId(entity.getOsdId())
			.setOsdName("-")
			.setOsdUsername("-")
			.setType(entity.getDstDeviceType().getName());
		return this;
	}
	
}
