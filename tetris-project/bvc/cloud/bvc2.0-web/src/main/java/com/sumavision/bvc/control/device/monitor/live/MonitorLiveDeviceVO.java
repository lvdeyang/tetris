package com.sumavision.bvc.control.device.monitor.live;

import com.sumavision.bvc.device.monitor.live.device.MonitorLiveDevicePO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

/**
 * 设备点播任务<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年6月24日 上午11:12:27
 */
public class MonitorLiveDeviceVO extends AbstractBaseVO<MonitorLiveDeviceVO, MonitorLiveDevicePO>{

	private String udpUrl;

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
	
	public String getUdpUrl() {
		return udpUrl;
	}

	public MonitorLiveDeviceVO setUdpUrl(String udpUrl) {
		this.udpUrl = udpUrl;
		return this;
	}

	public String getVideoBundleName() {
		return videoBundleName;
	}

	public MonitorLiveDeviceVO setVideoBundleName(String videoBundleName) {
		this.videoBundleName = videoBundleName;
		return this;
	}

	public String getVideoChannelName() {
		return videoChannelName;
	}

	public MonitorLiveDeviceVO setVideoChannelName(String videoChannelName) {
		this.videoChannelName = videoChannelName;
		return this;
	}

	public String getAudioBundleName() {
		return audioBundleName;
	}

	public MonitorLiveDeviceVO setAudioBundleName(String audioBundleName) {
		this.audioBundleName = audioBundleName;
		return this;
	}

	public String getAudioChannelName() {
		return audioChannelName;
	}

	public MonitorLiveDeviceVO setAudioChannelName(String audioChannelName) {
		this.audioChannelName = audioChannelName;
		return this;
	}

	public String getDstVideoBundleName() {
		return dstVideoBundleName;
	}

	public MonitorLiveDeviceVO setDstVideoBundleName(String dstVideoBundleName) {
		this.dstVideoBundleName = dstVideoBundleName;
		return this;
	}

	public String getDstVideoChannelName() {
		return dstVideoChannelName;
	}

	public MonitorLiveDeviceVO setDstVideoChannelName(String dstVideoChannelName) {
		this.dstVideoChannelName = dstVideoChannelName;
		return this;
	}

	public String getDstAudioBundleName() {
		return dstAudioBundleName;
	}

	public MonitorLiveDeviceVO setDstAudioBundleName(String dstAudioBundleName) {
		this.dstAudioBundleName = dstAudioBundleName;
		return this;
	}

	public String getDstAudioChannelName() {
		return dstAudioChannelName;
	}

	public MonitorLiveDeviceVO setDstAudioChannelName(String dstAudioChannelName) {
		this.dstAudioChannelName = dstAudioChannelName;
		return this;
	}
	
	public Long getOsdId() {
		return osdId;
	}

	public MonitorLiveDeviceVO setOsdId(Long osdId) {
		this.osdId = osdId;
		return this;
	}

	public String getOsdName() {
		return osdName;
	}

	public MonitorLiveDeviceVO setOsdName(String osdName) {
		this.osdName = osdName;
		return this;
	}

	public String getOsdUsername() {
		return osdUsername;
	}

	public MonitorLiveDeviceVO setOsdUsername(String osdUsername) {
		this.osdUsername = osdUsername;
		return this;
	}

	public String getType() {
		return type;
	}

	public MonitorLiveDeviceVO setType(String type) {
		this.type = type;
		return this;
	}

	@Override
	public MonitorLiveDeviceVO set(MonitorLiveDevicePO entity) throws Exception {
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
			.setDstAudioChannelName(entity.getDstAudioChannelName()==null?"-":entity.getDstAudioChannelName())
			.setOsdId(entity.getOsdId())
			.setOsdName("-")
			.setOsdUsername("-")
			.setType(entity.getDstDeviceType()==null?"":entity.getDstDeviceType().getName());
		return this;
	}
	
}
