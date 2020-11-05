package com.sumavision.bvc.control.device.monitor.live;

import com.sumavision.bvc.device.monitor.live.MonitorLiveSplitConfigPO;
import com.sumavision.tetris.commons.util.date.DateUtil;
import com.sumavision.tetris.mvc.converter.AbstractBaseVO;

public class MonitorLiveSplitConfigVO extends AbstractBaseVO<MonitorLiveSplitConfigVO, MonitorLiveSplitConfigPO>{

	/** 用户id */
	private Long userId;
	
	/** 屏幕序号 */
	private Integer serial;
	
	/************
	 ***视频目的***
	 ************/
	
	/** 目标设备id */
	private String dstVideoBundleId;
	
	/** 目标设备名称 */
	private String dstVideoBundleName;
	
	/** 目标设备类型 */
	private String dstVideoBundleType;
	
	/** 目标设备接入层id */
	private String dstVideoLayerId;
	
	/** 目标视频通道 */
	private String dstVideoChannelId;
	
	/** 目标视频通道类型 */
	private String dstVideoBaseType;
	
	/** 目标视频通道名称 */
	private String dstVideoChannelName;
	
	/************
	 ***音频目的***
	 ************/
	
	/** 目标音频设备id */
	private String dstAudioBundleId;
	
	/** 目标音频设备名称 */
	private String dstAudioBundleName;
	
	/** 目标音频设备类型 */
	private String dstAudioBundleType;
	
	/** 目标音频设备接入层 */
	private String dstAudioLayerId;
	
	/** 目标音频通道id */
	private String dstAudioChannelId;
	
	/** 目标音频通道类型 */
	private String dstAudioBaseType;
	
	/** 目标音频通道名称 */
	private String dstAudioChannelName;
	
	public Long getUserId() {
		return userId;
	}

	public MonitorLiveSplitConfigVO setUserId(Long userId) {
		this.userId = userId;
		return this;
	}

	public Integer getSerial() {
		return serial;
	}

	public MonitorLiveSplitConfigVO setSerial(Integer serial) {
		this.serial = serial;
		return this;
	}

	public String getDstVideoBundleId() {
		return dstVideoBundleId;
	}

	public MonitorLiveSplitConfigVO setDstVideoBundleId(String dstVideoBundleId) {
		this.dstVideoBundleId = dstVideoBundleId;
		return this;
	}

	public String getDstVideoBundleName() {
		return dstVideoBundleName;
	}

	public MonitorLiveSplitConfigVO setDstVideoBundleName(String dstVideoBundleName) {
		this.dstVideoBundleName = dstVideoBundleName;
		return this;
	}

	public String getDstVideoBundleType() {
		return dstVideoBundleType;
	}

	public MonitorLiveSplitConfigVO setDstVideoBundleType(String dstVideoBundleType) {
		this.dstVideoBundleType = dstVideoBundleType;
		return this;
	}

	public String getDstVideoLayerId() {
		return dstVideoLayerId;
	}

	public MonitorLiveSplitConfigVO setDstVideoLayerId(String dstVideoLayerId) {
		this.dstVideoLayerId = dstVideoLayerId;
		return this;
	}

	public String getDstVideoChannelId() {
		return dstVideoChannelId;
	}

	public MonitorLiveSplitConfigVO setDstVideoChannelId(String dstVideoChannelId) {
		this.dstVideoChannelId = dstVideoChannelId;
		return this;
	}

	public String getDstVideoBaseType() {
		return dstVideoBaseType;
	}

	public MonitorLiveSplitConfigVO setDstVideoBaseType(String dstVideoBaseType) {
		this.dstVideoBaseType = dstVideoBaseType;
		return this;
	}

	public String getDstVideoChannelName() {
		return dstVideoChannelName;
	}

	public MonitorLiveSplitConfigVO setDstVideoChannelName(String dstVideoChannelName) {
		this.dstVideoChannelName = dstVideoChannelName;
		return this;
	}

	public String getDstAudioBundleId() {
		return dstAudioBundleId;
	}

	public MonitorLiveSplitConfigVO setDstAudioBundleId(String dstAudioBundleId) {
		this.dstAudioBundleId = dstAudioBundleId;
		return this;
	}

	public String getDstAudioBundleName() {
		return dstAudioBundleName;
	}

	public MonitorLiveSplitConfigVO setDstAudioBundleName(String dstAudioBundleName) {
		this.dstAudioBundleName = dstAudioBundleName;
		return this;
	}

	public String getDstAudioBundleType() {
		return dstAudioBundleType;
	}

	public MonitorLiveSplitConfigVO setDstAudioBundleType(String dstAudioBundleType) {
		this.dstAudioBundleType = dstAudioBundleType;
		return this;
	}

	public String getDstAudioLayerId() {
		return dstAudioLayerId;
	}

	public MonitorLiveSplitConfigVO setDstAudioLayerId(String dstAudioLayerId) {
		this.dstAudioLayerId = dstAudioLayerId;
		return this;
	}

	public String getDstAudioChannelId() {
		return dstAudioChannelId;
	}

	public MonitorLiveSplitConfigVO setDstAudioChannelId(String dstAudioChannelId) {
		this.dstAudioChannelId = dstAudioChannelId;
		return this;
	}

	public String getDstAudioBaseType() {
		return dstAudioBaseType;
	}

	public MonitorLiveSplitConfigVO setDstAudioBaseType(String dstAudioBaseType) {
		this.dstAudioBaseType = dstAudioBaseType;
		return this;
	}

	public String getDstAudioChannelName() {
		return dstAudioChannelName;
	}

	public MonitorLiveSplitConfigVO setDstAudioChannelName(String dstAudioChannelName) {
		this.dstAudioChannelName = dstAudioChannelName;
		return this;
	}

	@Override
	public MonitorLiveSplitConfigVO set(MonitorLiveSplitConfigPO entity) throws Exception {
		this.setId(entity.getId())
			.setUuid(entity.getUuid())
			.setUpdateTime(entity.getUpdateTime()==null?"":DateUtil.format(entity.getUpdateTime(), DateUtil.dateTimePattern))
			.setDstVideoBaseType(entity.getDstVideoBaseType())
			.setDstVideoBundleId(entity.getDstVideoBundleId())
			.setDstVideoBundleName(entity.getDstVideoBundleName())
			.setDstVideoBundleType(entity.getDstVideoBundleType())
			.setDstVideoChannelId(entity.getDstVideoChannelId())
			.setDstVideoChannelName(entity.getDstVideoChannelName())
			.setDstVideoLayerId(entity.getDstVideoLayerId())
			.setDstAudioBaseType(entity.getDstAudioBaseType())
			.setDstAudioBundleId(entity.getDstAudioBundleId())
			.setDstAudioBundleName(entity.getDstAudioBundleName())
			.setDstAudioBundleType(entity.getDstAudioBundleType())
			.setDstAudioChannelId(entity.getDstAudioChannelId())
			.setDstAudioChannelName(entity.getDstAudioChannelName())
			.setDstAudioLayerId(entity.getDstAudioLayerId());
		return this;
	}
	
}
