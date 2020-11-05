package com.sumavision.bvc.device.monitor.live;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.sumavision.tetris.orm.po.AbstractBasePO;

/**
 * 点播页面屏幕关联设备配置<br/>
 * <b>作者:</b>lvdeyang<br/>
 * <b>版本：</b>1.0<br/>
 * <b>日期：</b>2019年10月30日 上午10:43:00
 */
@Entity
@Table(name = "BVC_MONITOR_LIVE_SPLIT_CONFIG")
public class MonitorLiveSplitConfigPO extends AbstractBasePO{

	/** 这是一个常量的说明 */
	private static final long serialVersionUID = 1L;

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
	
	@Column(name = "USER_ID")
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	@Column(name = "SERIAL")
	public Integer getSerial() {
		return serial;
	}

	public void setSerial(Integer serial) {
		this.serial = serial;
	}

	@Column(name = "DST_VIDEO_BUNDLE_ID")
	public String getDstVideoBundleId() {
		return dstVideoBundleId;
	}

	public void setDstVideoBundleId(String dstVideoBundleId) {
		this.dstVideoBundleId = dstVideoBundleId;
	}

	@Column(name = "DST_VIDEO_BUNDLE_NAME")
	public String getDstVideoBundleName() {
		return dstVideoBundleName;
	}

	public void setDstVideoBundleName(String dstVideoBundleName) {
		this.dstVideoBundleName = dstVideoBundleName;
	}

	@Column(name = "DST_VIDEO_BUNDLE_TYPE")
	public String getDstVideoBundleType() {
		return dstVideoBundleType;
	}

	public void setDstVideoBundleType(String dstVideoBundleType) {
		this.dstVideoBundleType = dstVideoBundleType;
	}

	@Column(name = "DST_VIDEO_LAYER_ID")
	public String getDstVideoLayerId() {
		return dstVideoLayerId;
	}

	public void setDstVideoLayerId(String dstVideoLayerId) {
		this.dstVideoLayerId = dstVideoLayerId;
	}

	@Column(name = "DST_VIDEO_CHANNEL_ID")
	public String getDstVideoChannelId() {
		return dstVideoChannelId;
	}

	public void setDstVideoChannelId(String dstVideoChannelId) {
		this.dstVideoChannelId = dstVideoChannelId;
	}

	@Column(name = "DST_VIDEO_BASE_TYPE")
	public String getDstVideoBaseType() {
		return dstVideoBaseType;
	}

	public void setDstVideoBaseType(String dstVideoBaseType) {
		this.dstVideoBaseType = dstVideoBaseType;
	}

	@Column(name = "DST_VIDEO_CHANNEL_NAME")
	public String getDstVideoChannelName() {
		return dstVideoChannelName;
	}

	public void setDstVideoChannelName(String dstVideoChannelName) {
		this.dstVideoChannelName = dstVideoChannelName;
	}

	@Column(name = "DST_AUDIO_BUNDLE_ID")
	public String getDstAudioBundleId() {
		return dstAudioBundleId;
	}

	public void setDstAudioBundleId(String dstAudioBundleId) {
		this.dstAudioBundleId = dstAudioBundleId;
	}

	@Column(name = "DST_AUDIO_BUNDLE_NAME")
	public String getDstAudioBundleName() {
		return dstAudioBundleName;
	}

	public void setDstAudioBundleName(String dstAudioBundleName) {
		this.dstAudioBundleName = dstAudioBundleName;
	}

	@Column(name = "DST_AUDIO_BUNDLE_TYPE")
	public String getDstAudioBundleType() {
		return dstAudioBundleType;
	}

	public void setDstAudioBundleType(String dstAudioBundleType) {
		this.dstAudioBundleType = dstAudioBundleType;
	}

	@Column(name = "DST_AUDIO_LAYER_ID")
	public String getDstAudioLayerId() {
		return dstAudioLayerId;
	}

	public void setDstAudioLayerId(String dstAudioLayerId) {
		this.dstAudioLayerId = dstAudioLayerId;
	}

	@Column(name = "DST_AUDIO_CHANNEL_ID")
	public String getDstAudioChannelId() {
		return dstAudioChannelId;
	}

	public void setDstAudioChannelId(String dstAudioChannelId) {
		this.dstAudioChannelId = dstAudioChannelId;
	}

	@Column(name = "DST_AUDIO_BASE_TYPE")
	public String getDstAudioBaseType() {
		return dstAudioBaseType;
	}

	public void setDstAudioBaseType(String dstAudioBaseType) {
		this.dstAudioBaseType = dstAudioBaseType;
	}

	@Column(name = "DST_AUDIO_CHANNEL_NAME")
	public String getDstAudioChannelName() {
		return dstAudioChannelName;
	}

	public void setDstAudioChannelName(String dstAudioChannelName) {
		this.dstAudioChannelName = dstAudioChannelName;
	}
	
}
